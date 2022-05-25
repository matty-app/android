package com.matryoshka.projectx.ui.launch

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LaunchScreenRouter(
    delayMillis: Long,
    navController: NavController,
    viewModel: LaunchViewModel = hiltViewModel()
) {
    LaunchScreen(
        delayMillis = delayMillis,
        onDelayFinished = { viewModel.checkUserSignedIn(navController) }
    )
}