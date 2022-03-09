package com.matryoshka.projectx.ui.launch

import androidx.compose.runtime.Composable

@Composable
fun SignInLaunchScreen(
    signIn: suspend () -> Unit
) {
    LaunchScreen {
        signIn()
    }
}