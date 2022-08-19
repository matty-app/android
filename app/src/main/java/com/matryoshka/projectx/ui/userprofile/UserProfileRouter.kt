package com.matryoshka.projectx.ui.userprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun UserProfileRouter(
    viewModel: UserProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.init(navController)
    }

    UserProfileScreen(
        navController = navController,
        state = viewModel.state,
        actions = viewModel.actions
    )
}