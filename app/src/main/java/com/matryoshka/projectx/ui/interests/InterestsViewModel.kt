package com.matryoshka.projectx.ui.interests

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey.INTERESTS_KEY
import com.matryoshka.projectx.SavedStateKey.SELECTED_INTERESTS_KEY
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.utils.exists
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestsViewModel @Inject constructor(
    private val interestsRepository: InterestsRepository,
) : ViewModel() {

    private var _isInitialized = false
    var state by mutableStateOf(InterestsScreenState(status = ScreenStatus.LOADING))
        private set

    suspend fun init(navController: NavController) {
        if (!_isInitialized) {
            try {
                val selectedInterests = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<List<Interest>>(INTERESTS_KEY) ?: emptyList()
                val interests = interestsRepository.getAll()
                    .map {
                        InterestState(
                            interest = it,
                            isSelected = selectedInterests.exists(it)
                        )
                    }
                state = state.copy(interests = interests, status = ScreenStatus.READY)
                _isInitialized = true
            } catch (ex: ProjectxException) {
                setErrorState(ex)
            }
        }
    }

    fun onSubmit(navController: NavController) {
        state = state.copy(status = ScreenStatus.LOADING)
        try {
            val selectedInterests = state.interests.asSequence()
                .filter { it.isSelected }
                .map { it.interest }
                .toList()
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set(SELECTED_INTERESTS_KEY, selectedInterests)
            navController.popBackStack()
        } catch (ex: ProjectxException) {
            setErrorState(ex)
        }
    }

    private fun setErrorState(error: ProjectxException) {
        state = state.copy(status = ScreenStatus.ERROR, error = error)
    }
}

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

@Stable
class InterestState(val interest: Interest, isSelected: Boolean) {

    var isSelected by mutableStateOf(isSelected)
        private set

    fun onChangeSelection() {
        isSelected = !isSelected
    }
}