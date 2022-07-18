package com.matryoshka.projectx.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.matryoshka.projectx.NavArgument.ARG_EMAIL
import com.matryoshka.projectx.NavArgument.ARG_EVENT_ID
import com.matryoshka.projectx.NavArgument.ARG_INTEREST_ID
import com.matryoshka.projectx.NavArgument.ARG_LOCATION
import com.matryoshka.projectx.data.event.Location
import com.matryoshka.projectx.data.map.LocationInfo
import com.matryoshka.projectx.ui.email.EmailConfirmationRouter
import com.matryoshka.projectx.ui.event.editing.EventEditingRouter
import com.matryoshka.projectx.ui.event.editing.InterestSelectionRouter
import com.matryoshka.projectx.ui.event.feed.EventsFeedRouter
import com.matryoshka.projectx.ui.event.viewing.EventViewingRouter
import com.matryoshka.projectx.ui.interests.InterestsRouter
import com.matryoshka.projectx.ui.launch.LaunchScreenRouter
import com.matryoshka.projectx.ui.launch.SignInLaunchScreenRouter
import com.matryoshka.projectx.ui.map.LocationSelectionRouter
import com.matryoshka.projectx.ui.map.viewing.LocationViewingRouter
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter
import com.matryoshka.projectx.ui.userprofile.UserProfileRouter

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
        route = "${Screen.EMAIL_CONFIRM}?$ARG_EMAIL={$ARG_EMAIL}",
        arguments = listOf(
            navArgument(ARG_EMAIL) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        EmailConfirmationRouter(
            navController = navController,
            email = backStackEntry.arguments?.getString(ARG_EMAIL) ?: ""
        )
    }

    composable(Screen.INTERESTS) {
        InterestsRouter(navController)
    }

    composable(Screen.EVENT_EDITING_SCREEN) {
        EventEditingRouter(navController = navController)
    }

    composable(
        route = "${Screen.EVENT_VIEWING_SCREEN}?$ARG_EVENT_ID={$ARG_EVENT_ID}",
        arguments = listOf(
            navArgument(ARG_EVENT_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        EventViewingRouter(
            navController = navController,
            eventId = backStackEntry.arguments?.getString(ARG_EVENT_ID) ?: ""
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
        route = "${Screen.LOCATION_VIEWING_SCREEN}?$ARG_LOCATION={$ARG_LOCATION}",
        arguments = listOf(navArgument(ARG_LOCATION) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val location = backStackEntry.arguments?.getString(ARG_LOCATION)?.let { json ->
            Gson().fromJson(json, Location::class.java)
        }
        LocationViewingRouter(
            navController = navController,
            viewModel = hiltViewModel(),
            location = location!!
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
        EventsFeedRouter(navController = navController)
    }
    composable(Screen.USER_PROFILE) {
        UserProfileRouter(navController = navController)
    }
}

fun NavController.currentScreenIs(screen: String) =
    currentDestination?.route == screen

object Screen {
    const val LAUNCH = "LAUNCH"
    const val SIGN_IN_LAUNCH = "SIGN_IN_LAUNCH"
    const val SIGN_UP = "SIGN_UP"
    const val SIGN_IN = "SIGN_IN"
    const val EMAIL_CONFIRM = "EMAIL_CONFIRM"
    const val INTERESTS = "INTERESTS"
    const val USER_PROFILE = "USER_PROFILE"
    const val LOCATION_SELECTION_SCREEN = "LOCATION_SELECTION_SCREEN"
    const val LOCATION_VIEWING_SCREEN = "LOCATION_VIEWING_SCREEN"
    const val INTEREST_SELECTION_SCREEN = "INTEREST_SELECTION_SCREEN"
    const val EVENT_EDITING_SCREEN = "EVENT_EDITING_SCREEN"
    const val EVENT_VIEWING_SCREEN = "EVENT_VIEWING_SCREEN"
    const val EVENTS_FEED_SCREEN = "EVENTS_FEED_SCREEN"
}