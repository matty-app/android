package com.matryoshka.projectx.ui.signin

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen.SIGN_UP

@Composable
fun SignInRouter(
    viewModel: SignInViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    SignInScreen(
        onSignUpClicked = { navAdapter.navigateTo(SIGN_UP) }
    )
}