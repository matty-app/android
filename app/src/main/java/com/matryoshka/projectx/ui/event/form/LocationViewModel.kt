package com.matryoshka.projectx.ui.event.form

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.matryoshka.projectx.SharedPrefKey.PREF_EVENT_FORM
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.ui.common.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "LocationViewModel"

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val navAdapter: NavAdapter
) : ViewModel() {
    var state by mutableStateOf(LocationUiState())
        private set

    private var eventState: EventState? = null

    fun init(sharedPreferences: SharedPreferences) {
        eventState = sharedPreferences.getString(PREF_EVENT_FORM, null)?.let { json ->
            Gson().fromJson(json, EventState::class.java)
        }
        state = state.copy(
            status = ScreenStatus.READY,
            location = eventState?.location ?: ""
        )
    }

    fun onSubmit(sharedPreferences: SharedPreferences) {
        eventState?.copy(
            location = state.location
        )?.writeToSharedPref(sharedPreferences, PREF_EVENT_FORM)
        Log.d(TAG, "onSubmit: $eventState")
        navAdapter.goBack()
    }

    fun onLocationChange(location: String) {
        state = state.copy(
            location = location
        )
    }
}

data class LocationUiState(
    val status: ScreenStatus = ScreenStatus.LOADING,
    val location: String = ""
)
