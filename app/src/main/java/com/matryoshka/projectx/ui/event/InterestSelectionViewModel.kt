package com.matryoshka.projectx.ui.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey.INTEREST_KEY

class InterestSelectionViewModel : ViewModel() {
    var state by mutableStateOf(InterestSelectionState(loading = true))
        private set

    fun onInit() {
        state = state.copy(loading = false, interests = interestsMock)
    }

    fun onInterestClick(name: String) {
        state = if (state.selectedInterest == name) {
            state.copy(selectedInterest = null)
        } else {
            state.copy(selectedInterest = name)
        }
    }

    fun onSubmit(navController: NavController) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(INTEREST_KEY, state.selectedInterest)
        navController.popBackStack()
    }

    fun onCancel(navController: NavController) {
        navController.popBackStack()
    }
}

data class InterestSelectionState(
    val loading: Boolean,
    val interests: List<String> = emptyList(),
    val selectedInterest: String? = null
)

private val interestsMock = listOf(
    "Football",
    "Chess",
    "Traveling"
)