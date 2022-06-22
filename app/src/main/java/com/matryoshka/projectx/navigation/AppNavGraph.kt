package com.matryoshka.projectx.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.matryoshka.projectx.NavArgument.ARG_EMAIL
import com.matryoshka.projectx.ui.email.EmailConfirmationRouter
import com.matryoshka.projectx.ui.event.InterestSelectionScreen
import com.matryoshka.projectx.ui.event.InterestSelectionViewModel
import com.matryoshka.projectx.ui.event.NewEventScreen
import com.matryoshka.projectx.ui.event.NewEventScreenViewModel
import com.matryoshka.projectx.ui.feed.EventsFeedScreen
import com.matryoshka.projectx.ui.interests.InterestsRouter
import com.matryoshka.projectx.ui.launch.LaunchScreenRouter
import com.matryoshka.projectx.ui.launch.SignInLaunchScreenRouter
import com.matryoshka.projectx.ui.map.LocationSelectionScreen
import com.matryoshka.projectx.ui.map.LocationSelectionViewModel
import com.matryoshka.projectx.ui.signin.SignInRouter
import com.matryoshka.projectx.ui.signup.SignUpRouter

@OptIn(ExperimentalAnimationApi::class)
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
        val viewModel: NewEventScreenViewModel = viewModel()
        NewEventScreen(
            navController = navController,
            state = viewModel.state,
            eventFormActions = viewModel.eventFormActions,
            onInit = viewModel::init
        )
    }

    composable(Screen.LOCATION_SELECTION_SCREEN) {
        val viewModel: LocationSelectionViewModel = hiltViewModel()
        LocationSelectionScreen(
            state = viewModel.state,
            onSubmit = { viewModel.onSubmit(navController) },
            onCancel = { viewModel.onCancel(navController) },
            onCancelingSearch = viewModel::onCancelingSearch,
            onSuggestionClick = viewModel::onSuggestionClick,
            displayUserLocation = viewModel::displayUserLocation
        )
    }

    composable(Screen.INTEREST_SELECTION_SCREEN) {
        val viewModel: InterestSelectionViewModel = viewModel()
        InterestSelectionScreen(
            state = viewModel.state,
            onInit = viewModel::onInit,
            onInterestClick = viewModel::onInterestClick,
            onSubmit = { viewModel.onSubmit(navController) },
            onCancel = { viewModel.onCancel(navController) }
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