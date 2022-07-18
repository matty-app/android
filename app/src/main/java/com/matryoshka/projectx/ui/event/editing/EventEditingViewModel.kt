package com.matryoshka.projectx.ui.event.editing

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.matryoshka.projectx.NavArgument.ARG_INTEREST_ID
import com.matryoshka.projectx.NavArgument.ARG_LOCATION
import com.matryoshka.projectx.SavedStateKey.INTEREST_KEY
import com.matryoshka.projectx.SavedStateKey.LOCATION_KEY
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.data.event.EventsRepository
import com.matryoshka.projectx.data.event.Location
import com.matryoshka.projectx.navigation.Screen.INTEREST_SELECTION_SCREEN
import com.matryoshka.projectx.navigation.Screen.LOCATION_SELECTION_SCREEN
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.ScreenStatus.LOADING
import com.matryoshka.projectx.ui.common.ScreenStatus.READY
import com.matryoshka.projectx.ui.common.ScreenStatus.SUBMITTING
import com.matryoshka.projectx.ui.event.editing.form.EventFormActions
import com.matryoshka.projectx.ui.event.editing.form.EventFormState
import com.matryoshka.projectx.utils.collectOnce
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "NewEventViewModel"

@HiltViewModel
class EventEditingViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {
    var state by mutableStateOf(EventEditingState())
        private set

    val formActions = EventFormActions(
        onLocationClick = { navController ->
            viewModelScope.launch {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.collectOnce(
                        key = LOCATION_KEY,
                        initialValue = state.formState.locationField.value,
                        consumer = state.formState.locationField::onChange
                    )
            }
            val locationJson = Gson().toJson(state.formState.locationField.value)
            navController.navigate("$LOCATION_SELECTION_SCREEN?$ARG_LOCATION=$locationJson")
        },
        onInterestClick = { navController ->
            viewModelScope.launch {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.collectOnce(
                        key = INTEREST_KEY,
                        initialValue = state.formState.interestField.value,
                        consumer = state.formState.interestField::onChange
                    )
            }
            navController.navigate("$INTEREST_SELECTION_SCREEN?$ARG_INTEREST_ID=${state.formState.interestField.value?.id ?: ""}")
        }
    )

    fun onSubmit(navController: NavController) {
        val form = state.formState
        val event = with(state.formState) {
            Event(
                name = nameField.value,
                summary = summaryField.value,
                details = detailsField.value,
                interest = state.formState.interestField.value!!,
                public = isPublicField.value,
                maxParticipants = maxParticipantsField.value?.toIntOrNull(),
                location = Location(
                    name = locationField.value?.name,
                    address = locationField.value?.address,
                    coordinates = locationField.value?.geoData?.coordinates
                ),
                startDate = form.startDateField.value,
                endDate = form.endDateField.value,
                withApproval = form.withApprovalField.value
            )
        }
        viewModelScope.launch {
            val result = eventsRepository.save(event)
            Log.d(TAG, "onSubmit: $result")
            navController.popBackStack()
        }
    }

    fun init() {
        state = state.copy(status = READY)
        Log.d(TAG, "init: $state")
    }
}

@Stable
data class EventEditingState(
    val status: ScreenStatus = LOADING,
    val formState: EventFormState = EventFormState()
) {
    val displayForm: Boolean
        get() = status != LOADING
    val showProgress
        get() = status == LOADING || status == SUBMITTING
}