package com.matryoshka.projectx.ui.event.editing

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey.INTEREST_KEY
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.ui.common.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "InterestSelectionVM"

@HiltViewModel
class InterestSelectionViewModel @Inject constructor(
    private val interestsRepository: InterestsRepository
) : ViewModel() {
    var state by mutableStateOf(InterestSelectionState(status = ScreenStatus.LOADING))
        private set

    init {
        viewModelScope.launch {
            val interests = interestsRepository.getAll()
            state = state.copy(interests = interests)
        }

    }

    fun init(selectedInterestId: String?) {
        Log.d(TAG, "init: selectedInterestId: $selectedInterestId")
        state = state.copy(status = ScreenStatus.READY, selectedInterestId = selectedInterestId)
    }

    fun onInterestClick(navController: NavController, interest: Interest) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(INTEREST_KEY, interest)
        navController.popBackStack()
    }

    fun onCancel(navController: NavController) {
        navController.popBackStack()
    }
}

data class InterestSelectionState(
    val status: ScreenStatus,
    val interests: List<Interest> = emptyList(),
    val selectedInterestId: String? = null
)