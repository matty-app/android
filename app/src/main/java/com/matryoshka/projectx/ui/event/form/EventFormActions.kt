package com.matryoshka.projectx.ui.event.form

import com.matryoshka.projectx.ui.common.NAV_FOR_RESULT_DEFAULT_ACTION
import com.matryoshka.projectx.ui.common.NavForResultAction

data class EventFormActions(
    val onLocationClick: NavForResultAction = NAV_FOR_RESULT_DEFAULT_ACTION,
    val onInterestClick: NavForResultAction = NAV_FOR_RESULT_DEFAULT_ACTION,
)