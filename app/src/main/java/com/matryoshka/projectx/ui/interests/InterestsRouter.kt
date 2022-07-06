package com.matryoshka.projectx.ui.interests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun InterestsRouter(
    navController: NavController,
    viewModel: InterestsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.init(navController)
    }

    InterestsScreen(
        state = viewModel.state,
        onBackClick = { navController.popBackStack() },
        onSubmit = { viewModel.onSubmit(navController) }
    )
}