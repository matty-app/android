package com.matryoshka.projectx.ui.interests

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.data.user.UsersRepository
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(
    authService: AuthService,
    interestsRepository: InterestsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val currentUser = authService.getCurrentUser()!!

    var state by mutableStateOf(InterestsScreenState(status = ScreenStatus.LOADING))
        private set

    init {
        viewModelScope.launch {
            try {
                val interests = interestsRepository.getAll().map { InterestState(it) }
                state = state.copy(interests = interests, status = ScreenStatus.READY)
            } catch (ex: ProjectxException) {
                setErrorState(ex)
            }
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            state = state.copy(status = ScreenStatus.LOADING)
            try {
                usersRepository.save(
                    currentUser.copy(
                        interests = state.interests.asSequence()
                            .filter { it.isSelected }
                            .map { it.interest }
                            .toList()
                    )
                )
                state = state.copy(status = ScreenStatus.READY)
            } catch (ex: ProjectxException) {
                setErrorState(ex)
            }
        }
    }

    private fun setErrorState(error: ProjectxException) {
        state = state.copy(status = ScreenStatus.ERROR, error = error)
    }
}

@Stable
data class InterestsScreenState(
    val interests: List<InterestState> = emptyList(),
    val status: ScreenStatus = ScreenStatus.READY,
    val error: ProjectxException? = null
) {
    val isProgressIndicatorVisible: Boolean
        get() = status == ScreenStatus.LOADING

    val isErrorToastVisible: Boolean
        get() = status == ScreenStatus.ERROR && error != null
}

class InterestState(val interest: Interest) {

    var isSelected by mutableStateOf(false)
        private set

    fun onChangeSelection() {
        isSelected = !isSelected
    }
}