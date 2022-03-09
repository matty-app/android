package com.matryoshka.projectx.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.matryoshka.projectx.ui.email.EmailConfirmationRouter
import com.matryoshka.projectx.ui.interests.InterestsRouter
import com.matryoshka.projectx.ui.launch.LaunchScreen
import com.matryoshka.projectx.ui.launch.SignInLaunchScreenRouter
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter

fun NavGraphBuilder.appNavGraph(
    navAdapter: NavAdapter
) {
    composable(Screen.LAUNCH) {
        LaunchScreen(
            delayMillis = 2500,
            onDelayFinished = { navAdapter.navigateTo(Screen.SIGN_UP) }
        )
    }
    composable(Screen.SIGN_IN_LAUNCH) {
        SignInLaunchScreenRouter(navAdapter = navAdapter)
    }
    composable(Screen.SIGN_UP) {
        SignUpRouter(navAdapter = navAdapter)
    }
    composable(Screen.SIGN_IN) {
        SignInRouter(navAdapter = navAdapter)
    }
    composable(
        route = "${Screen.EMAIL_CONFIRM}?$ARG_EMAIL={email}",
        arguments = listOf(navArgument(ARG_EMAIL) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        EmailConfirmationRouter(
            email = backStackEntry.arguments?.getString(ARG_EMAIL) ?: "",
            navAdapter = navAdapter
        )
    }
    composable(Screen.INTERESTS) {
        InterestsRouter()
    }
}

object Screen {
    const val LAUNCH = "LAUNCH"
    const val SIGN_IN_LAUNCH = "SIGN_IN_LAUNCH"
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
}

const val ARG_EMAIL = "email"