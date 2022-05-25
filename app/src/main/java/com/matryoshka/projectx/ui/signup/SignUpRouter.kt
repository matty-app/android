package com.matryoshka.projectx.ui.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.Screen

@Composable
fun SignUpRouter(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    SignUpScreen(
        state = viewModel.state,
        onRegisterClicked = { viewModel.onRegisterClicked(navController) },
        onSignInClicked = { navController.navigate(Screen.SIGN_IN) }
    )
}