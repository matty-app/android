package com.matryoshka.projectx.ui.viewmodel

import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.support.CoroutineDispatcherRule
import com.matryoshka.projectx.ui.launch.LaunchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LaunchViewModelTest {
    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    @Test
    fun `should go to sign up screen if user is not signed in`() = runTest {
        val authService = mockk<AuthService>().apply {
            coEvery { getCurrentUser() } returns null
        }
        val viewModel = LaunchViewModel(authService)

//        viewModel.checkUserSignedIn()
//
//        verify { navAdapter.navigateTo(Screen.SIGN_UP) }
    }

    @Test
    fun `should go to interests screen if user is signed in`() = runTest {
        val authService = mockk<AuthService>().apply {
            coEvery { getCurrentUser() } returns User("1", "John", "john@gmail.com")
        }
//        val navAdapter = mockk<NavAdapter>().apply {
//            justRun { navigateTo(any()) }
//        }
//        val viewModel = LaunchViewModel(authService, navAdapter)

//        viewModel.checkUserSignedIn()

//        verify { navAdapter.navigateTo(Screen.INTERESTS) }
    }
}