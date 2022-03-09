package com.matryoshka.projectx.ui.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.setSignInEmail

@Composable
fun SignInRouter(
    viewModel: SignInViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    val context = LocalContext.current

    SignInScreen(
        emailField = viewModel.emailField,
        onLogInClicked = {
            if (viewModel.validate()) {
                val email = viewModel.emailField.value!!

                context.setSignInEmail(email)
                viewModel.sendLinkToEmail(email)
                navAdapter.goToEmailConfirmationScreen(email)
            }
        },
        onSignUpClicked = { navAdapter.navigateTo(Screen.SIGN_UP) }
    )
}