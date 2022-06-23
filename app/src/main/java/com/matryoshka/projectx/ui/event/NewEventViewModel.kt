package com.matryoshka.projectx.ui.event

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.matryoshka.projectx.NavArgument.ARG_LOCATION
import com.matryoshka.projectx.SavedStateKey.INTEREST_KEY
import com.matryoshka.projectx.SavedStateKey.LOCATION_KEY
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.navigation.Screen.LOCATION_SELECTION_SCREEN
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.ScreenStatus.LOADING
import com.matryoshka.projectx.ui.common.ScreenStatus.READY
import com.matryoshka.projectx.ui.common.ScreenStatus.SUBMITTING
import com.matryoshka.projectx.ui.event.form.EventFormActions
import com.matryoshka.projectx.ui.event.form.EventFormState
import com.matryoshka.projectx.utils.observeOnce

private const val TAG = "NewEventViewModel"

class NewEventScreenViewModel : ViewModel() {
    var state by mutableStateOf(NewEventState())
        private set

    val eventFormActions = EventFormActions(
        onLocationClick = { navController, lifecycleOwner ->
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.observeOnce<LocationInfo>(
                    lifecycleOwner,
                    LOCATION_KEY,
                    state.formState.location::onChange
                )
            val locationArg = Gson().toJson(state.formState.location.value)
            navController.navigate("$LOCATION_SELECTION_SCREEN?$ARG_LOCATION=$locationArg")
        },
        onInterestClick = { navController, lifecycleOwner ->
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.observeOnce<String>(
                    lifecycleOwner,
                    INTEREST_KEY,
                    state.formState.interest::onChange
                )
            navController.navigate(Screen.INTEREST_SELECTION_SCREEN)
        }
    )

    fun init() {
        state = state.copy(status = READY)
        Log.d(TAG, "init: $state")
    }
}

@Stable
data class NewEventState(
    val status: ScreenStatus = LOADING,
    val formState: EventFormState = EventFormState()
) {
    val displayForm: Boolean
        get() = status != LOADING
    val showProgress
        get() = status == LOADING || status == SUBMITTING
}