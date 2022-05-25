package com.matryoshka.projectx.ui.launch

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.navToEventsFeedScreen
import com.matryoshka.projectx.ui.common.ScreenStatus

@Composable
fun SignInLaunchScreenRouter(
    navController: NavController,
    viewModel: SignInLaunchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity

    SignInLaunchScreen(
        status = viewModel.status,
        error = viewModel.error,
        signIn = {
            viewModel.signInByEmailLink(activity.intent)
            if (viewModel.status != ScreenStatus.ERROR) {
                navController.navToEventsFeedScreen()
            }
        }
    )
}