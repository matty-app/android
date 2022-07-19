package com.matryoshka.projectx.ui.event.editing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.data.event.Event

@Composable
fun EventEditingRouter(
    viewModel: EventEditingViewModel = hiltViewModel(),
    event: Event?,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.init(event)
    }

    EventEditingScreen(
        navController = navController,
        state = viewModel.state,
        eventFormActions = viewModel.formActions,
        onSubmit = { viewModel.onSubmit(navController) }
    )
}