package com.matryoshka.projectx.ui.viewmodel

import android.content.Intent
import android.content.SharedPreferences
import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.data.repository.UsersRepository
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.support.CoroutineDispatcherRule
import com.matryoshka.projectx.support.assert
import com.matryoshka.projectx.support.callPrivateFunction
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.isNewUser
import com.matryoshka.projectx.ui.common.userEmail
import com.matryoshka.projectx.ui.common.userName
import com.matryoshka.projectx.ui.launch.SignInLaunchViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class SignInLaunchViewModelTest {
    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    private val user = User(
        uid = "1",
        name = "John",
        email = "john@gmail.com"
    )
    private val link = "https://"

    @Test
    fun `should set error`() {
        val viewModel = createViewModel()
        val expectedException = ProjectxException()
        val expectedStatus = ScreenStatus.ERROR

        viewModel.callPrivateFunction("setErrorState", arrayOf(expectedException))

        with(viewModel) {
            assertEquals(expectedException, error)
            assertEquals(expectedStatus, status)
        }
    }

    @Test
    fun `should create user if new user`() = runTest {
        val sharedPrefs = mockk<SharedPreferences>().apply {
            every { userEmail } returns user.email
            every { userName } returns user.name
            every { isNewUser } returns true
        }
        val authService = mockk<AuthService>().apply {
            coEvery { signUpByEmailLink(user.email!!, user.name!!, link) } returns user
        }
        val usersRepository = mockk<UsersRepository>().apply {
            coJustRun { save(any()) }
        }
        val intent = mockk<Intent>().apply {
            every { data.toString() } returns link
        }
        val viewModel = createViewModel(authService, usersRepository, sharedPrefs)

        val actualUser = viewModel.signInByEmailLink(intent)

        coVerify { authService.signUpByEmailLink(user.email!!, user.name!!, link) }
        coVerify { usersRepository.save(user) }
        assertEquals(user, actualUser)
    }

    @Test
    fun `should sign in user if not new user`() = runTest {
        val sharedPrefs = mockk<SharedPreferences>().apply {
            every { userEmail } returns user.email
            every { isNewUser } returns false
        }
        val authService = mockk<AuthService>().apply {
            coEvery { signInByEmailLink(user.email!!, link) } returns user
        }
        val intent = mockk<Intent>().apply {
            every { data.toString() } returns link
        }
        val viewModel = createViewModel(authService = authService, sharedPrefs = sharedPrefs)

        val actualUser = viewModel.signInByEmailLink(intent)

        coVerify { authService.signInByEmailLink(user.email!!, link) }
        assertEquals(user, actualUser)
    }

    @Test
    fun `should set error if exception is thrown`() = runTest {
        val sharedPrefs = mockk<SharedPreferences>().apply {
            every { userEmail } returns user.email
            every { isNewUser } returns false
        }
        val authService = mockk<AuthService>().apply {
            coEvery { signInByEmailLink(user.email!!, link) } throws Exception()
        }
        val intent = mockk<Intent>().apply {
            every { data.toString() } returns link
        }
        val viewModel = createViewModel(authService = authService, sharedPrefs = sharedPrefs)

        val actualUser = viewModel.signInByEmailLink(intent)

        with(viewModel) {
            error.assert(ProjectxException())
            assertEquals(ScreenStatus.ERROR, status)
        }
        assertNull(actualUser)
    }

    private fun createViewModel(
        authService: AuthService = mockk(),
        usersRepository: UsersRepository = mockk(),
        sharedPrefs: SharedPreferences = mockk()
    ) = SignInLaunchViewModel(authService, usersRepository, sharedPrefs)
}