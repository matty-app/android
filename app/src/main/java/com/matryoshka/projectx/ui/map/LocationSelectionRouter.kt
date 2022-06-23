package com.matryoshka.projectx.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.matryoshka.projectx.data.map.LocationInfo

@Composable
fun LocationSelectionRouter(
    navController: NavController,
    viewModel: LocationSelectionViewModel,
    location: LocationInfo?
) {
    LaunchedEffect(Unit) {
        viewModel.init(location)
    }

    LocationSelectionScreen(
        state = viewModel.state,
        onSubmit = { viewModel.onSubmit(navController) },
        onCancel = { viewModel.onCancel(navController) },
        onCancelingSearch = viewModel::onCancelingSearch,
        onSuggestionClick = viewModel::onSuggestionClick,
        displayUserLocation = viewModel::displayUserLocation
    )
}