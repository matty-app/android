package com.matryoshka.projectx.ui.launch

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.ScreenStatus

@Composable
fun SignInLaunchScreenRouter(
    viewModel: SignInLaunchViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    val context = LocalContext.current
    val activity = context as Activity

    SignInLaunchScreen(
        status = viewModel.status,
        error = viewModel.error,
        signIn = {
            viewModel.signInByEmailLink(activity.intent)
            if (viewModel.status != ScreenStatus.ERROR) {
                navAdapter.navigateTo(Screen.INTERESTS)
            }
        }
    )
}