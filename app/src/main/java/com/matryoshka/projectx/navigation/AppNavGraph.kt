package com.matryoshka.projectx.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matryoshka.projectx.ui.signup.SignUpRouter

fun NavGraphBuilder.appNavGraph(
    navAdapter: NavAdapter
) {
    composable(Screen.SIGN_UP) {
        SignUpRouter()
    }
}

object Screen {
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
}