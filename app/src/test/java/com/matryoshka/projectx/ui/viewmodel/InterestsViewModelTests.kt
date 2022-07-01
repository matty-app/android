package com.matryoshka.projectx.ui.viewmodel

import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.data.user.UsersRepository
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.support.CoroutineDispatcherRule
import com.matryoshka.projectx.support.assert
import com.matryoshka.projectx.support.callPrivateFunction
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.interests.InterestState
import com.matryoshka.projectx.ui.interests.InterestsViewModel
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class InterestsViewModelTests {
    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    private val user = User(
        id = "1",
        name = "John",
        email = "john@gmail.com"
    )

    private val interests = listOf(
        Interest("coding", "Coding"),
        Interest("sport", "Sport")
    )

    @Test
    fun `should set error`() {
        val viewModel = createViewModel()
        val expectedException = ProjectxException()
        val expectedStatus = ScreenStatus.ERROR

        viewModel.callPrivateFunction("setErrorState", arrayOf(expectedException))

        with(viewModel) {
            assertEquals(expectedException, state.error)
            assertEquals(expectedStatus, state.status)
        }
    }

    @Test
    fun `should get interests`() = runTest {
        val viewModel = createViewModel()

        assertEquals(2, viewModel.state.interests.size)
    }

    @Test
    fun `should set error if exception is thrown when interests are getting`() = runTest {
        val interestsRepository = mockk<InterestsRepository>().apply {
            coEvery { getAll() } throws ProjectxException()
        }

        val viewModel = createViewModel(interestsRepository = interestsRepository)

        with(viewModel) {
            state.error.assert(ProjectxException())
            assertEquals(ScreenStatus.ERROR, state.status)
        }
    }

    @Test
    fun `should save user with selected interests`() = runTest {
        val usersRepository = mockk<UsersRepository>().apply {
            coJustRun { save(any()) }
        }
        val viewModel = createViewModel(usersRepository = usersRepository).apply {
            state.interests[0].onChangeSelection()
        }

        viewModel.onNextClick()

        coVerify {
            usersRepository.save(user.copy(interests = listOf(interests[0])))
        }
    }

    @Test
    fun `should set error if exception is thrown when user is saving`() {
        val usersRepository = mockk<UsersRepository>().apply {
            coEvery { save(any()) } throws ProjectxException()
        }
        val viewModel = createViewModel(usersRepository = usersRepository).apply {
            state.interests[0].onChangeSelection()
        }

        viewModel.onNextClick()

        with(viewModel) {
            state.error.assert(ProjectxException())
            assertEquals(ScreenStatus.ERROR, state.status)
        }
    }

    @Test
    fun `should change selection for InterestState`() {
        val interest = Interest("coding", "Coding")
        val interestState = InterestState(interest)

        interestState.onChangeSelection()

        assertTrue(interestState.isSelected)
    }

    private fun createViewModel(
        authService: AuthService = mockk<AuthService>().apply {
            every { getCurrentUser() } returns user
        },
        interestsRepository: InterestsRepository = mockk<InterestsRepository>().apply {
            coEvery { getAll() } returns interests
        },
        usersRepository: UsersRepository = mockk()
    ) = InterestsViewModel(authService, interestsRepository, usersRepository)
}