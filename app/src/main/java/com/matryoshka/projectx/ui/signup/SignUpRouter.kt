package com.matryoshka.projectx.ui.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignUpRouter(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    SignUpScreen()
}