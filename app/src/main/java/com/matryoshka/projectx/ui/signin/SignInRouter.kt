package com.matryoshka.projectx.ui.signin

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.Screen

@Composable
fun SignInRouter(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    SignInScreen(
        state = viewModel.state,
        onLogInClicked = { viewModel.onLogInClicked(navController) },
        onSignUpClicked = { navController.navigate(Screen.SIGN_UP) }
    )
}