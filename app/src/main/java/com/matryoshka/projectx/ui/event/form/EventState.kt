package com.matryoshka.projectx.ui.event.form

import android.content.SharedPreferences
import androidx.compose.runtime.Stable
import com.google.gson.Gson
import com.matryoshka.projectx.SharedPrefKey.PREF_EVENT_FORM

@Stable
data class EventState(
    val title: String = "",
    val summary: String = "",
    val details: String = "",
    val public: Boolean = true,
    val interest: String? = null,
    val location: String? = null,
    val limitMaxParticipants: Boolean = false,
    val maxParticipants: Int? = null
)

fun EventState.writeToSharedPref(
    sharedPreferences: SharedPreferences,
    key: String = PREF_EVENT_FORM
) {
    val eventJson = Gson().toJson(this)
    sharedPreferences.edit()
        .putString(key, eventJson)
        .apply()
}