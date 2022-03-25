package com.matryoshka.projectx.ui.event.form

import android.content.SharedPreferences

data class EventFormActions(
    val onLocationClick: (sharedPref: SharedPreferences) -> Unit = {},
    val onTitleChange: (String) -> Unit = {},
    val onSummaryChange: (String) -> Unit = {},
    val onDetailsChange: (String) -> Unit = {},
    val onPublicChange: (Boolean) -> Unit = {},
    val onLimitMaxParticipantsChange: (Boolean) -> Unit = {},
    val onMaxParticipantsChange: (String) -> Unit = {}
)