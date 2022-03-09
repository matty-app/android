package com.matryoshka.projectx.ui.launch

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.signInEmail

@Composable
fun SignInLaunchScreenRouter(
    viewModel: SignInLaunchViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    val context = LocalContext.current
    val email = context.signInEmail!!
    val activity = context as Activity
    val link = activity.intent.data.toString()

    SignInLaunchScreen(
        signIn = {
            val user = viewModel.signInByEmailLink(email, link)

            if (user == null) {
                //TODO navigate to error screen
            }
            navAdapter.navigateTo(Screen.INTERESTS)
        }
    )
}