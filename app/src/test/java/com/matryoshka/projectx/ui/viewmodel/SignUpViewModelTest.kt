package com.matryoshka.projectx.ui.viewmodel

import android.content.SharedPreferences
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.support.CoroutineDispatcherRule
import com.matryoshka.projectx.support.callPrivateFunction
import com.matryoshka.projectx.support.callPrivateSuspendFunction
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.setIsNewUser
import com.matryoshka.projectx.ui.common.setUserEmail
import com.matryoshka.projectx.ui.common.setUserName
import com.matryoshka.projectx.ui.signup.SignUpViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SignUpViewModelTest {
    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    @Test
    fun `should set error`() {
        val viewModel = createViewModel()
        val exception = ProjectxException()
        val status = ScreenStatus.ERROR

        viewModel.callPrivateFunction("setError", arrayOf(exception))

        assertEquals(exception, viewModel.state.error)
        assertEquals(status, viewModel.state.status)
    }

    @Test
    fun `should change status`() {
        val expectedStatus = ScreenStatus.ERROR
        val viewModel = createViewModel()

        viewModel.callPrivateFunction("changeStatus", arrayOf(expectedStatus))

        assertEquals(expectedStatus, viewModel.state.status)
    }

    @Test
    fun `should not be valid if name is empty`() = runTest {
        val viewModel = createViewModel().apply {
            state.nameField.onChange("")
        }

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertFalse(isValid)
    }

    @Test
    fun `should not be valid if email is empty`() = runTest {
        val viewModel = createViewModel().apply {
            state.emailField.onChange("")
        }

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertFalse(isValid)
    }

    @Test
    fun `should not be valid if email is null`() = runTest {
        val viewModel = createViewModel()

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertFalse(isValid)
    }

    @Test
    fun `should not be valid if email has bad format`() = runTest {
        val viewModel = createViewModel().apply {
            state.emailField.onChange("nnn@")
        }

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertFalse(isValid)
    }

    @Test
    fun `should not be valid if email exists`() = runTest {
        val email = "john@gmail.com"
        val name = "John"
        val authService = mockk<AuthService>().apply {
            coEvery { checkEmailExists(email) } returns true
        }
        val viewModel = createViewModel(authService = authService).apply {
            state.nameField.onChange(name)
            state.emailField.onChange(email)
        }

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertFalse(isValid)
    }

    @Test
    fun `should be valid`() = runTest {
        val email = "john@gmail.com"
        val name = "John"
        val authService = mockk<AuthService>().apply {
            coEvery { checkEmailExists(email) } returns false
        }
        val viewModel = createViewModel(authService = authService).apply {
            state.nameField.onChange(name)
            state.emailField.onChange(email)
        }

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertTrue(isValid)
    }

    @Test
    fun `should send sign in link to email`() = runTest {
        val email = "john@gmail.com"
        val authService = mockk<AuthService>().apply {
            coJustRun { sendSignInLinkToEmail(any()) }
        }
        val viewModel = createViewModel(authService = authService).apply {
            state.emailField.onChange(email)
        }

        viewModel.callPrivateSuspendFunction("sendLinkToEmail", arrayOf(email))

        coVerify { authService.sendSignInLinkToEmail(email) }
    }

    @Test
    fun `should save shared preferences`() = runTest {
        val email = "john@gmail.com"
        val name = "John"
        val sharedPrefs = mockk<SharedPreferences>().apply {
            justRun { setUserEmail(email) }
            justRun { setUserName(name) }
            justRun { setIsNewUser(true) }
        }
        val viewModel = createViewModel(sharedPrefs = sharedPrefs).apply {
            state.emailField.onChange(email)
            state.nameField.onChange(name)
        }

        viewModel.callPrivateFunction("saveSignUpPrefs", arrayOf(email, name))

        with(sharedPrefs) {
            verify { setUserEmail(email) }
            verify { setUserName(name) }
            verify { setIsNewUser(true) }
        }
    }

    @Test
    fun `should go to EmailConfirmationScreen`() = runTest {
        val email = "john@gmail.com"
        val navAdapter = mockk<NavAdapter>().apply {
            justRun { goToEmailConfirmationScreen(any()) }
        }
        val viewModel = createViewModel(navAdapter = navAdapter).apply {
            state.emailField.onChange(email)
        }

        viewModel.callPrivateFunction("goToEmailConfirmationScreen", arrayOf(email))

        verify { navAdapter.goToEmailConfirmationScreen(email) }
    }

    @Test
    fun `should send link to email and go to EmailConfirmationScreen after register clicked`() =
        runTest {
            val email = "john@gmail.com"
            val name = "John"
            val viewModel = spyk(createViewModel(), recordPrivateCalls = true).also {
                every { it invokeNoArgs "validate" } returns true
                justRun {
                    it invoke "saveSignUpPrefs" withArguments listOf(any<String>(), any<String>())
                }
                justRun { it invoke "goToEmailConfirmationScreen" withArguments listOf(any<String>()) }
                justRun { it invoke "sendLinkToEmail" withArguments listOf(any<String>()) }
                it.state.nameField.onChange(name)
                it.state.emailField.onChange(email)
            }

            viewModel.onRegisterClicked()

            viewModel.let {
                verify { it invoke "sendLinkToEmail" withArguments listOf(email) }
                verify { it invoke "saveSignUpPrefs" withArguments listOf(email, name) }
                verify { it invoke "goToEmailConfirmationScreen" withArguments listOf(email) }
            }
        }

    @Test
    fun `should set error if exception occurred after register clicked`() = runTest {
        val email = "john@gmail.com"
        val name = "John"
        val expectedError = ProjectxException()
        val expectedStatus = ScreenStatus.ERROR

        val viewModel = spyk(createViewModel(), recordPrivateCalls = true).also {
            every { it invokeNoArgs "validate" } returns true
            justRun {
                it invoke "saveSignUpPrefs" withArguments listOf(any<String>(), any<String>())
            }
            justRun { it invoke "goToEmailConfirmationScreen" withArguments listOf(any<String>()) }
            every { it invoke "sendLinkToEmail" withArguments listOf(any<String>()) } throws ProjectxException()
            it.state.nameField.onChange(name)
            it.state.emailField.onChange(email)
        }

        viewModel.onRegisterClicked()

        assertEquals(expectedError, viewModel.state.error)
        assertEquals(expectedStatus, viewModel.state.status)
        verify(exactly = 0) {
            viewModel invoke "goToEmailConfirmationScreen" withArguments listOf(email)
        }
    }

    @Test
    fun `should not go to EmailConfirmationScreen and send link if not valid after register clicked`() =
        runTest {
            val email = "john@gmail.com"
            val name = "John"
            val viewModel = spyk(createViewModel(), recordPrivateCalls = true).also {
                every { it invokeNoArgs "validate" } returns false
                justRun {
                    it invoke "saveSignUpPrefs" withArguments listOf(any<String>(), any<String>())
                }
                justRun { it invoke "goToEmailConfirmationScreen" withArguments listOf(any<String>()) }
                justRun { it invoke "sendLinkToEmail" withArguments listOf(any<String>()) }
                it.state.nameField.onChange(name)
                it.state.emailField.onChange(email)
            }

            viewModel.onRegisterClicked()

            viewModel.let {
                verify(exactly = 0) { it invoke "sendLinkToEmail" withArguments listOf(email) }
                verify(exactly = 0) {
                    it invoke "goToEmailConfirmationScreen" withArguments listOf(email)
                }
            }
        }

    private fun createViewModel(
        authService: AuthService = mockk(),
        sharedPrefs: SharedPreferences = mockk(),
        navAdapter: NavAdapter = mockk()
    ) = SignUpViewModel(authService, sharedPrefs, navAdapter)
}