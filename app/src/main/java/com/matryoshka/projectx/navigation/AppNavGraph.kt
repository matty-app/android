package com.matryoshka.projectx.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matryoshka.projectx.navigation.Screen.SIGN_IN
import com.matryoshka.projectx.navigation.Screen.SIGN_UP
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter

fun NavGraphBuilder.appNavGraph(
    navAdapter: NavAdapter
) {
    composable(SIGN_UP) {
        SignUpRouter(navAdapter = navAdapter)
    }
    composable(SIGN_IN) {
        SignInRouter(navAdapter = navAdapter)
    }
}

object Screen {
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
}