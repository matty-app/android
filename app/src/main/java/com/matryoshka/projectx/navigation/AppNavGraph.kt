package com.matryoshka.projectx.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matryoshka.projectx.ui.email.EmailConfirmationRouter
import com.matryoshka.projectx.ui.interests.InterestsRouter
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter

fun NavGraphBuilder.appNavGraph(
    navAdapter: NavAdapter
) {
    composable(Screen.SIGN_UP) {
        SignUpRouter(navAdapter = navAdapter)
    }
    composable(Screen.SIGN_IN) {
        SignInRouter(navAdapter = navAdapter)
    }
    composable(Screen.EMAIL_CONFIRM) {
        EmailConfirmationRouter(navAdapter = navAdapter)
    }
    composable(Screen.INTERESTS) {
        InterestsRouter()
    }
}

object Screen {
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
}