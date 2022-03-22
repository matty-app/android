package com.matryoshka.projectx.ui.launch

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

    SignInLaunchScreen(
        status = viewModel.status,
        error = viewModel.error,
        signIn = {
            viewModel.signInByEmailLink(context)
            if (viewModel.status != ScreenStatus.ERROR) {
                navAdapter.navigateTo(Screen.INTERESTS)
            }
        }
    )
}