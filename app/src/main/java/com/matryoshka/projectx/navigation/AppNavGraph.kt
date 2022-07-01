package com.matryoshka.projectx.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.matryoshka.projectx.NavArgument.ARG_EMAIL
import com.matryoshka.projectx.NavArgument.ARG_INTEREST_ID
import com.matryoshka.projectx.NavArgument.ARG_LOCATION
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.ui.email.EmailConfirmationRouter
import com.matryoshka.projectx.ui.event.InterestSelectionRouter
import com.matryoshka.projectx.ui.event.NewEventScreen
import com.matryoshka.projectx.ui.event.NewEventScreenViewModel
import com.matryoshka.projectx.ui.feed.EventsFeedScreen
import com.matryoshka.projectx.ui.interests.InterestsRouter
import com.matryoshka.projectx.ui.launch.LaunchScreenRouter
import com.matryoshka.projectx.ui.launch.SignInLaunchScreenRouter
import com.matryoshka.projectx.ui.map.LocationSelectionRouter
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter

fun NavGraphBuilder.appNavGraph(navController: NavController) {

    composable(Screen.LAUNCH) {
        LaunchScreenRouter(delayMillis = 2500, navController)
    }

    composable(Screen.SIGN_IN_LAUNCH) {
        SignInLaunchScreenRouter(navController)
    }

    composable(Screen.SIGN_UP) {
        SignUpRouter(navController)
    }

    composable(Screen.SIGN_IN) {
        SignInRouter(navController)
    }

    composable(
        route = "${Screen.EMAIL_CONFIRM}?$ARG_EMAIL={email}",
        arguments = listOf(navArgument(ARG_EMAIL) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        EmailConfirmationRouter(
            navController = navController,
            email = backStackEntry.arguments?.getString(ARG_EMAIL) ?: ""
        )
    }

    composable(Screen.INTERESTS) {
        InterestsRouter()
    }

    composable(Screen.NEW_EVENT_SCREEN) {
        val viewModel: NewEventScreenViewModel = hiltViewModel()
        NewEventScreen(
            navController = navController,
            state = viewModel.state,
            eventFormActions = viewModel.formActions,
            onInit = viewModel::init,
            onSubmit = viewModel::onSubmit
        )
    }

    composable(
        route = "${Screen.LOCATION_SELECTION_SCREEN}?$ARG_LOCATION={$ARG_LOCATION}",
        arguments = listOf(navArgument(ARG_LOCATION) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val location = backStackEntry.arguments?.getString(ARG_LOCATION)?.let { json ->
            Gson().fromJson(json, LocationInfo::class.java)
        }
        LocationSelectionRouter(
            navController = navController,
            viewModel = hiltViewModel(),
            location = location
        )
    }

    composable(
        route = "${Screen.INTEREST_SELECTION_SCREEN}?$ARG_INTEREST_ID={$ARG_INTEREST_ID}",
        arguments = listOf(navArgument(ARG_INTEREST_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val selectedInterestId = backStackEntry.arguments?.getString(ARG_INTEREST_ID)
        InterestSelectionRouter(
            navController = navController,
            interestId = selectedInterestId,
            viewModel = hiltViewModel()
        )
    }

    composable(Screen.EVENTS_FEED_SCREEN) {
        EventsFeedScreen(navController)
    }
}

object Screen {
    const val LAUNCH = "LAUNCH"
    const val SIGN_IN_LAUNCH = "SIGN_IN_LAUNCH"
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
    const val LOCATION_SELECTION_SCREEN = "LOCATION_SELECTION_SCREEN"
    const val INTEREST_SELECTION_SCREEN = "INTEREST_SELECTION_SCREEN"
    const val NEW_EVENT_SCREEN = "NEW_EVENT_SCREEN"
    const val EVENTS_FEED_SCREEN = "EVENTS_FEED_SCREEN"
}