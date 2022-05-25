package com.matryoshka.projectx.ui.event.form

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController

typealias NavForResultAction = (NavController, LifecycleOwner) -> Unit

private val NAV_FOR_RESULT_DEFAULT_ACTION: NavForResultAction = { _, _ -> }

data class EventFormActions(
    val onLocationClick: NavForResultAction = NAV_FOR_RESULT_DEFAULT_ACTION,
    val onInterestClick: NavForResultAction = NAV_FOR_RESULT_DEFAULT_ACTION,
)