package com.matryoshka.projectx.ui.email

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen

@Composable
fun EmailConfirmationRouter(
    viewModel: EmailConfirmationViewModel = hiltViewModel(),
    email: String,
    navAdapter: NavAdapter
) {
    EmailConfirmationScreen(
        email = email,
        onBackClicked = navAdapter::goBack,
        onSendAgainClicked = { navAdapter.navigateTo(Screen.INTERESTS) }
    )
}