package com.matryoshka.projectx.ui.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen.SIGN_IN

@Composable
fun SignUpRouter(
    viewModel: SignUpViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    SignUpScreen(
        onSignInClicked = {
            navAdapter.navigateTo(SIGN_IN)
        }
    )
}