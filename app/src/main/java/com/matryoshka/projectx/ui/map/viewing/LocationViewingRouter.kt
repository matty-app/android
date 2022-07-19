package com.matryoshka.projectx.ui.map.viewing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.data.event.Location
import com.matryoshka.projectx.utils.toLocationInfo

@Composable
fun LocationViewingRouter(
    viewModel: LocationViewingViewModel = hiltViewModel(),
    location: Location,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.init(location.toLocationInfo())
    }

    LocationViewingScreen(
        state = viewModel.state,
        onCancel = { navController.popBackStack() }
    )
}