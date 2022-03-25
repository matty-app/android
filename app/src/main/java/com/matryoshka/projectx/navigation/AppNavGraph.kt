package com.matryoshka.projectx.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.matryoshka.projectx.NavArgument.ARG_EMAIL
import com.matryoshka.projectx.ui.email.EmailConfirmationRouter
import com.matryoshka.projectx.ui.event.NewEventScreen
import com.matryoshka.projectx.ui.event.NewEventScreenViewModel
import com.matryoshka.projectx.ui.event.form.LocationScreen
import com.matryoshka.projectx.ui.event.form.LocationViewModel
import com.matryoshka.projectx.ui.interests.InterestsRouter
import com.matryoshka.projectx.ui.launch.LaunchScreenRouter
import com.matryoshka.projectx.ui.launch.SignInLaunchScreenRouter
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter

fun NavGraphBuilder.appNavGraph(
    navAdapter: NavAdapter
) {
    composable(Screen.LAUNCH) {
        LaunchScreenRouter(delayMillis = 2500)
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
    composable(Screen.NEW_EVENT_SCREEN) {
        val viewModel: NewEventScreenViewModel = hiltViewModel()
        NewEventScreen(
            state = viewModel.state,
            eventFormActions = viewModel.eventFormActions,
            onInit = viewModel::init
        )
    }
    composable(Screen.LOCATION_SCREEN) {
        val viewModel: LocationViewModel = hiltViewModel()
        LocationScreen(
            state = viewModel.state,
            onInit = viewModel::init,
            onSubmit = viewModel::onSubmit,
            onLocationChange = viewModel::onLocationChange
        )
    }
}

object Screen {
    const val LAUNCH = "LAUNCH"
    const val SIGN_IN_LAUNCH = "SIGN_IN_LAUNCH"
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
    const val LOCATION_SCREEN = "LOCATION_SCREEN"
    const val NEW_EVENT_SCREEN = "NEW_EVENT_SCREEN"
}