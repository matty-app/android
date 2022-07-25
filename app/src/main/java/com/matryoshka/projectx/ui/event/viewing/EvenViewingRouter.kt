package com.matryoshka.projectx.ui.event.viewing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.navToLocationViewingScreen

@Composable
fun EventViewingRouter(
    viewModel: EventViewingViewModel = hiltViewModel(),
    eventId: String,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.init(eventId)
    }

    EventViewingScreen(
        state = viewModel.state,
        onBackClick = { navController.popBackStack() },
        onEditClick = { viewModel.onEditClick(navController) },
        onLocationClick = { location -> navController.navToLocationViewingScreen(location) }
    )
}