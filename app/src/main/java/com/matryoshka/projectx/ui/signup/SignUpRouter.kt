package com.matryoshka.projectx.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.setSignInEmail

@Composable
fun SignUpRouter(
    viewModel: SignUpViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    val context = LocalContext.current

    SignUpScreen(
        nameField = viewModel.nameField,
        emailField = viewModel.emailField,
        onRegisterClicked = {
            if (viewModel.validate()) {
                val email = viewModel.emailField.value!!

                context.setSignInEmail(email)
                viewModel.sendLinkToEmail(email)
                navAdapter.goToEmailConfirmationScreen(email)
            }
        },
        onSignInClicked = { navAdapter.navigateTo(Screen.SIGN_IN) }
    )
}