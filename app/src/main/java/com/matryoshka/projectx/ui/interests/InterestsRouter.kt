package com.matryoshka.projectx.ui.interests

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InterestsRouter(
    viewModel: InterestsViewModel = hiltViewModel()
) {
    InterestsScreen(state = viewModel.state, onNextClicked = viewModel::onNextClick)
}