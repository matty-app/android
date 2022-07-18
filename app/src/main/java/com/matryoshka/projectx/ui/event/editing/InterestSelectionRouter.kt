package com.matryoshka.projectx.ui.event.editing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun InterestSelectionRouter(
    navController: NavController,
    interestId: String?,
    viewModel: InterestSelectionViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.init(interestId)
    }

    InterestSelectionScreen(
        state = viewModel.state,
        onInterestClick = { interest -> viewModel.onInterestClick(navController, interest) },
        onCancel = { viewModel.onCancel(navController) }
    )
}