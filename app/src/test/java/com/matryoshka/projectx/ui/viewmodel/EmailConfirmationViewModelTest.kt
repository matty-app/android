package com.matryoshka.projectx.ui.viewmodel

import com.matryoshka.projectx.support.CoroutineDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
class EmailConfirmationViewModelTest {
    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

//    @Test
//    fun `should set error`() {
//        val viewModel = createViewModel()
//        val expectedException = ProjectxException()
//        val status = ScreenStatus.ERROR
//
//        viewModel.callPrivateFunction("setError", arrayOf(status, expectedException))
//
//        with(viewModel) {
//            assertEquals(expectedException, state.error)
//            assertEquals(status, state.status)
//        }
//    }
//
//    @Test
//    fun `should change status`() {
//        val expectedStatus = ScreenStatus.ERROR
//        val viewModel = createViewModel()
//
//        viewModel.callPrivateFunction("changeStatus", arrayOf(expectedStatus))
//
//        assertEquals(expectedStatus, viewModel.state.status)
//    }
//
//    @Test
//    fun `should send link to email on send again clicked`() =
//        runTest {
//            val email = "john@gmail.com"
//            val authService = mockk<AuthService>().apply {
//                coJustRun { sendSignInLinkToEmail(email) }
//            }
//            val viewModel = createViewModel(authService = authService)
//
//            viewModel.onSendAgainClicked(email)
//
//            coVerify { authService.sendSignInLinkToEmail(email) }
//        }
//
//    @Test
//    fun `should set error if exception occurred on send again clicked`() = runTest {
//        val email = "john@gmail.com"
//        val expectedError = ProjectxException()
//        val expectedStatus = ScreenStatus.ERROR
//        val authService = mockk<AuthService>().apply {
//            coEvery { sendSignInLinkToEmail(email) } throws ProjectxException()
//        }
//        val viewModel = createViewModel(authService = authService)
//
//        viewModel.onSendAgainClicked(email)
//
//        with(viewModel) {
//            state.error.assert(expectedError)
//            assertEquals(expectedStatus, state.status)
//        }
//    }
//
//    private fun createViewModel(
//        authService: AuthService = mockk(),
//    ) = EmailConfirmationViewModel(authService)
}