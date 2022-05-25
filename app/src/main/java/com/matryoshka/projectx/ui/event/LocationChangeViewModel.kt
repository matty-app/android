package com.matryoshka.projectx.ui.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.SavedStateKey.LOCATION_KEY
import com.matryoshka.projectx.ui.common.ScreenStatus

class LocationChangeViewModel : ViewModel() {
    var state by mutableStateOf(LocationChangeState(location = ""))
        private set

    fun onSubmit(navController: NavController) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(LOCATION_KEY, state.location)
        navController.popBackStack()
    }

    fun onCancel(navController: NavController) {
        navController.popBackStack()
    }

    fun onLocationChange(location: String) {
        state = state.copy(
            location = location
        )
    }
}

data class LocationChangeState(
    val location: String = ""
)