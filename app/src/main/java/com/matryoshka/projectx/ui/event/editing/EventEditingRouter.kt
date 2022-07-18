package com.matryoshka.projectx.ui.event.editing

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun EventEditingRouter(
    viewModel: EventEditingViewModel = hiltViewModel(),
    navController: NavController
) {
    EventEditingScreen(
        navController = navController,
        state = viewModel.state,
        eventFormActions = viewModel.formActions,
        onInit = viewModel::init,
        onSubmit =  { viewModel.onSubmit(navController) }
    )
}