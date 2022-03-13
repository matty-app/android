package com.matryoshka.projectx.ui.launch

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LaunchScreenRouter(
    delayMillis: Long,
    viewModel: LaunchViewModel = hiltViewModel()
) {
    LaunchScreen(
        delayMillis = delayMillis,
        onDelayFinished = viewModel::checkUserSignedIn
    )
}