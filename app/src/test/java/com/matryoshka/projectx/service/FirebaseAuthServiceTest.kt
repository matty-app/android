package com.matryoshka.projectx.service

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.exception.CheckEmailExistsException
import com.matryoshka.projectx.exception.SendSignInLinkToEmailException
import com.matryoshka.projectx.exception.SignInByEmailLinkException
import com.matryoshka.projectx.exception.SignUpByEmailLinkException
import com.matryoshka.projectx.exception.UpdateUserException
import com.matryoshka.projectx.support.taskMock
import com.matryoshka.projectx.support.voidObject
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class FirebaseAuthServiceTest {
    private val testUserUid = "123456"
    private val testUserName = "John"
    private val testUserEmail = "john@gmail.com"
    private val authLink = "https://"
    private val testUser = User(testUserUid, testUserName, testUserEmail)

    private val firebaseUser: FirebaseUser
        get() = mockk<FirebaseUser>().apply {
            every { uid } returns testUserUid
            every { displayName } returns testUserName
            every { email } returns testUserEmail
        }

    private val authResult: AuthResult
        get() = mockk<AuthResult>().apply {
            every { user } returns firebaseUser
        }

    @Test
    fun `should call sendSignInLinkToEmail() from FirebaseAuth`() = runTest {
        val task = taskMock(voidObject)
        val firebaseAuth = mockk<FirebaseAuth>().apply {
            every { sendSignInLinkToEmail(any(), any()) } returns task
        }
        val authService = FirebaseAuthService(firebaseAuth)

        authService.sendSignInLinkToEmail(testUserEmail)

        verify { firebaseAuth.sendSignInLinkToEmail(testUserEmail, any()) }
    }

    @Test
    fun `sendSignInLinkToEmail should throw SendSignInLinkToEmailException if error occurred`() =
        runTest {
            val firebaseAuth = mockk<FirebaseAuth>().apply {
                every { sendSignInLinkToEmail(testUserEmail, any()) } returns taskMock(
                    voidObject,
                    Exception()
                )
            }
            val authService = FirebaseAuthService(firebaseAuth)

            assertFailsWith<SendSignInLinkToEmailException> {
                authService.sendSignInLinkToEmail(testUserEmail)
            }
        }

    @Test
    fun `should call isSignInWithEmailLink() from FirebaseAuth`() {
        val firebaseAuth = mockk<FirebaseAuth>().apply {
            every { isSignInWithEmailLink(authLink) } returns true
        }
        val authService = FirebaseAuthService(firebaseAuth)

        val result = authService.isSignInWithEmailLink(authLink)

        verify { firebaseAuth.isSignInWithEmailLink(authLink) }
        assertTrue(result)
    }

    @Test
    fun `signInByEmailLink should return user`() = runTest {
        val firebaseAuth = mockk<FirebaseAuth>().apply {
            every { signInWithEmailLink(testUserEmail, authLink) } returns taskMock(authResult)
        }
        val authService = FirebaseAuthService(firebaseAuth)

        val user = authService.signInByEmailLink(testUserEmail, authLink)

        verify { firebaseAuth.signInWithEmailLink(testUserEmail, authLink) }
        assertEquals(testUser, user)
    }

    @Test
    fun `signInByEmailLink should throw SignInByEmailLinkException if no user returned`() =
        runTest {
            val authResult = mockk<AuthResult> {
                every { user } returns null
            }
            val firebaseAuth = mockk<FirebaseAuth>().apply {
                every { signInWithEmailLink(testUserEmail, authLink) } returns taskMock(authResult)
            }
            val authService = FirebaseAuthService(firebaseAuth)

            assertFailsWith<SignInByEmailLinkException> {
                authService.signInByEmailLink(testUserEmail, authLink)
            }
        }

    @Test
    fun `signInByEmailLink should throw SignInByEmailLinkException if error occurred`() =
        runTest {
            val firebaseAuth = mockk<FirebaseAuth>().apply {
                every { signInWithEmailLink(testUserEmail, authLink) } returns taskMock(
                    authResult,
                    Exception()
                )
            }
            val authService = FirebaseAuthService(firebaseAuth)

            assertFailsWith<SignInByEmailLinkException> {
                authService.signInByEmailLink(testUserEmail, authLink)
            }
        }

    @Test
    fun `should update user`() =
        runTest {
            val newName = "Greg"
            val newUser = testUser.copy(name = newName)
            val firebaseUser = mockk<FirebaseUser>().apply {
                every { updateProfile(any()) } returns taskMock(voidObject)
            }
            val firebaseAuth = mockk<FirebaseAuth>().apply {
                every { currentUser } returns firebaseUser
            }
            val authService = FirebaseAuthService(firebaseAuth)

            authService.updateUser(newUser)

            verify { firebaseUser.updateProfile(match { it.displayName == newName }) }
        }

    @Test
    fun `updateUser should throw UpdateUserException if error occurred`() =
        runTest {
            val newUser = testUser.copy(name = "Greg")
            val firebaseUser = mockk<FirebaseUser>().apply {
                every { updateProfile(any()) } throws Exception()
            }
            val firebaseAuth = mockk<FirebaseAuth>().apply {
                every { currentUser } returns firebaseUser
            }
            val authService = FirebaseAuthService(firebaseAuth)

            assertFailsWith<UpdateUserException> { authService.updateUser(newUser) }
        }

    @Test
    fun `updateUser should throw UpdateUserException if user is signed out`() =
        runTest {
            val expectedMessage = "User ${testUser.id} is signed out"
            val firebaseAuth = mockk<FirebaseAuth>().apply {
                every { currentUser } returns null
            }
            val authService = FirebaseAuthService(firebaseAuth)

            assertFailsWith<UpdateUserException>(expectedMessage) {
                authService.updateUser(testUser)
            }
        }

    @Test
    fun `signUpByEmailLink should return user`() = runTest {
        val createdUserName = "Greg"
        val authService = spyk(FirebaseAuthService(mockk())).apply {
            coEvery { signInByEmailLink(any(), any()) } returns testUser
            coJustRun { updateUser(any()) }
        }

        val createdUser = authService.signUpByEmailLink(testUserEmail, createdUserName, authLink)

        assertEquals(testUser.copy(name = createdUserName), createdUser)
    }

    @Test
    fun `signUpByEmailLink should throw SignUpByEmailLinkException if error occurred`() = runTest {
        val createdUserName = "Greg"
        val authService = spyk(FirebaseAuthService(mockk())).apply {
            coEvery { signInByEmailLink(any(), any()) } returns testUser
            coEvery { updateUser(any()) } throws Exception()
        }

        assertFailsWith<SignUpByEmailLinkException> {
            authService.signUpByEmailLink(testUserEmail, createdUserName, authLink)
        }
    }

    @Test
    fun `checkEmailExists should return true if email exists`() = runTest {
        val signInMethodQueryResult = mockk<SignInMethodQueryResult>().apply {
            every { signInMethods } returns listOf("method1", "method2")
        }
        val firebaseAuth = mockk<FirebaseAuth> {
            every { fetchSignInMethodsForEmail(testUserEmail) } returns taskMock(
                signInMethodQueryResult
            )
        }
        val authService = FirebaseAuthService(firebaseAuth)

        assertTrue(authService.checkEmailExists(testUserEmail))
    }

    @Test
    fun `checkEmailExists should return false if email not exists`() = runTest {
        val signInMethodQueryResult = mockk<SignInMethodQueryResult>().apply {
            every { signInMethods } returns listOf()
        }
        val firebaseAuth = mockk<FirebaseAuth> {
            every { fetchSignInMethodsForEmail(testUserEmail) } returns taskMock(
                signInMethodQueryResult
            )
        }
        val authService = FirebaseAuthService(firebaseAuth)

        assertFalse(authService.checkEmailExists(testUserEmail))
    }

    @Test
    fun `checkEmailExists should throw CheckEmailExistsException if error occurred`() = runTest {
        val firebaseAuth = mockk<FirebaseAuth> {
            every { fetchSignInMethodsForEmail(testUserEmail) } throws Exception()
        }
        val authService = FirebaseAuthService(firebaseAuth)

        assertFailsWith<CheckEmailExistsException> { authService.checkEmailExists(testUserEmail) }
    }

    @Test
    fun `should return current user`() {
        val firebaseAuth = mockk<FirebaseAuth>().apply {
            every { currentUser } returns firebaseUser
        }
        val authService = FirebaseAuthService(firebaseAuth)

        val currentUser = authService.getCurrentUser()

        assertEquals(testUser, currentUser)
    }

    @Test
    fun `getCurrentUser should return null if user is signed out`() {
        val firebaseAuth = mockk<FirebaseAuth>().apply {
            every { currentUser } returns null
        }
        val authService = FirebaseAuthService(firebaseAuth)

        assertNull(authService.getCurrentUser())
    }
}