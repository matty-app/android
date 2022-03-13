package com.matryoshka.projectx.ui.email

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter

@Composable
fun EmailConfirmationRouter(
    viewModel: EmailConfirmationViewModel = hiltViewModel(),
    email: String,
    navAdapter: NavAdapter
) {
    EmailConfirmationScreen(
        state = viewModel.state,
        email = email,
        onBackClicked = navAdapter::goBack,
        onSendAgainClicked = viewModel::onSendAgainClicked
    )
}