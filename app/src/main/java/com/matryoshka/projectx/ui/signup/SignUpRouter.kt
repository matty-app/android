package com.matryoshka.projectx.ui.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen

@Composable
fun SignUpRouter(
    viewModel: SignUpViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    SignUpScreen(
        onRegisterClicked = { navAdapter.navigateTo(Screen.EMAIL_CONFIRM) },
        onSignInClicked = { navAdapter.navigateTo(Screen.SIGN_IN) }
    )
}