package com.matryoshka.projectx.ui.event.form

import androidx.navigation.NavController

data class EventFormActions(
    val onLocationClick: (navController: NavController) -> Unit = {},
    val onInterestClick: (navController: NavController) -> Unit = {},
)