package com.matryoshka.projectx.ui.event

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.matryoshka.projectx.SharedPrefKey.PREF_EVENT_FORM
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.ScreenStatus.LOADING
import com.matryoshka.projectx.ui.common.ScreenStatus.READY
import com.matryoshka.projectx.ui.common.ScreenStatus.SUBMITTING
import com.matryoshka.projectx.ui.event.form.EventFormActions
import com.matryoshka.projectx.ui.event.form.EventState
import com.matryoshka.projectx.ui.event.form.writeToSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "NewEventViewModel"

@HiltViewModel
class NewEventScreenViewModel @Inject constructor(
    private val navAdapter: NavAdapter
) : ViewModel() {
    var state by mutableStateOf(NewEventUiState())
        private set

    val eventFormActions = EventFormActions(
        onLocationClick = this::onLocationClick,
        onTitleChange = createFieldHandler { state.event.copy(title = it) },
        onSummaryChange = createFieldHandler { state.event.copy(summary = it) },
        onDetailsChange = createFieldHandler { state.event.copy(details = it) },
        onPublicChange = createFieldHandler { state.event.copy(public = it) },
        onLimitMaxParticipantsChange = createFieldHandler { state.event.copy(limitMaxParticipants = it) },
        onMaxParticipantsChange = createFieldHandler { state.event.copy(maxParticipants = it.toIntOrNull()) }
    )

    fun init(sharedPreferences: SharedPreferences) {
        val json = sharedPreferences.getString(PREF_EVENT_FORM, null)
        val event = json?.let { Gson().fromJson(json, EventState::class.java) } ?: state.event
        state = state.copy(
            status = READY,
            event = event
        )
        Log.d(TAG, "init: $state")
    }

    private fun <T> createFieldHandler(producer: (T) -> EventState): (T) -> Unit = {
        state = state.copy(event = producer(it))
        Log.d(TAG, "newFieldHandler: $state")
    }

    private fun onLocationClick(sharedPreferences: SharedPreferences) {
        state.event.writeToSharedPref(sharedPreferences)
        navAdapter.navigateTo(
            route = Screen.LOCATION_SCREEN
        )
    }
}


@Stable
data class NewEventUiState(
    val status: ScreenStatus = LOADING,
    val event: EventState = EventState(),
) {
    val displayForm: Boolean
        get() = status != LOADING
    val showProgress
        get() = status == LOADING || status == SUBMITTING
}