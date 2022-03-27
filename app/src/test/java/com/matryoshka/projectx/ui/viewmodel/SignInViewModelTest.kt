package com.matryoshka.projectx.ui.viewmodel

import android.content.SharedPreferences
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.support.CoroutineDispatcherRule
import com.matryoshka.projectx.support.assert
import com.matryoshka.projectx.support.callPrivateFunction
import com.matryoshka.projectx.support.callPrivateSuspendFunction
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.setIsNewUser
import com.matryoshka.projectx.ui.common.setUserEmail
import com.matryoshka.projectx.ui.signin.SignInViewModel
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
class SignInViewModelTest {
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
    fun `should not be valid if email not exists`() = runTest {
        val email = "john@gmail.com"
        val authService = mockk<AuthService>().apply {
            coEvery { checkEmailExists(email) } returns false
        }
        val viewModel = createViewModel(authService = authService).apply {
            state.emailField.onChange(email)
        }

        val isValid = viewModel.callPrivateSuspendFunction("validate") as Boolean

        assertFalse(isValid)
    }

    @Test
    fun `should be valid`() = runTest {
        val email = "john@gmail.com"
        val authService = mockk<AuthService>().apply {
            coEvery { checkEmailExists(email) } returns true
        }
        val viewModel = createViewModel(authService = authService).apply {
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
        val sharedPrefs = mockk<SharedPreferences>().apply {
            justRun { setUserEmail(email) }
            justRun { setIsNewUser(false) }
        }
        val viewModel = createViewModel(sharedPrefs = sharedPrefs).apply {
            state.emailField.onChange(email)
        }

        viewModel.callPrivateFunction("saveSignInPrefs", arrayOf(email))

        with(sharedPrefs) {
            verify { setUserEmail(email) }
            verify { setIsNewUser(false) }
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
    fun `should send link to email and go to EmailConfirmationScreen after log in clicked`() =
        runTest {
            val email = "john@gmail.com"
            val viewModel = spyk(createViewModel(), recordPrivateCalls = true).also {
                every { it invokeNoArgs "validate" } returns true
                justRun { it invoke "saveSignInPrefs" withArguments listOf(any<String>()) }
                justRun { it invoke "goToEmailConfirmationScreen" withArguments listOf(any<String>()) }
                justRun { it invoke "sendLinkToEmail" withArguments listOf(any<String>()) }
                it.state.emailField.onChange(email)
            }

            viewModel.onLogInClicked()

            viewModel.let {
                verify { it invoke "sendLinkToEmail" withArguments listOf(email) }
                verify { it invoke "saveSignInPrefs" withArguments listOf(email) }
                verify { it invoke "goToEmailConfirmationScreen" withArguments listOf(email) }
            }
        }

    @Test
    fun `should set error if exception occurred after log in clicked`() = runTest {
        val email = "john@gmail.com"
        val expectedError = ProjectxException()
        val expectedStatus = ScreenStatus.ERROR
        val viewModel = spyk(createViewModel(), recordPrivateCalls = true).also {
            every { it invokeNoArgs "validate" } returns true
            every { it invoke "sendLinkToEmail" withArguments listOf(any<String>()) } throws ProjectxException()
            justRun { it invoke "saveSignInPrefs" withArguments listOf(any<String>()) }
            justRun { it invoke "goToEmailConfirmationScreen" withArguments listOf(any<String>()) }
            it.state.emailField.onChange(email)
        }

        viewModel.onLogInClicked()

        viewModel.state.error.assert(expectedError)
        assertEquals(expectedStatus, viewModel.state.status)
        verify(exactly = 0) {
            viewModel invoke "goToEmailConfirmationScreen" withArguments listOf(email)
        }
    }

    @Test
    fun `should not send link if email is not valid after log in clicked`() =
        runTest {
            val email = "john@com"
            val viewModel = spyk(createViewModel(), recordPrivateCalls = true).also {
                every { it invokeNoArgs "validate" } returns false
                justRun { it invoke "sendLinkToEmail" withArguments listOf(any<String>()) }
                justRun { it invoke "saveSignInPrefs" withArguments listOf(any<String>()) }
                justRun { it invoke "goToEmailConfirmationScreen" withArguments listOf(any<String>()) }
                it.state.emailField.onChange(email)
            }

            viewModel.onLogInClicked()

            viewModel.let {
                verify(exactly = 0) { it invoke "sendLinkToEmail" withArguments listOf(email) }
                verify(exactly = 0) { it invoke "saveSignInPrefs" withArguments listOf(email) }
                verify(exactly = 0) {
                    it invoke "goToEmailConfirmationScreen" withArguments listOf(email)
                }
            }
        }

    private fun createViewModel(
        authService: AuthService = mockk(),
        sharedPrefs: SharedPreferences = mockk(),
        navAdapter: NavAdapter = mockk()
    ) = SignInViewModel(authService, sharedPrefs, navAdapter)
}