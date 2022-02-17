package com.matryoshka.projectx.ui.email

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.matryoshka.projectx.navigation.NavAdapter

@Composable
fun EmailConfirmationRouter(
    viewModel: EmailConfirmationViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    EmailConfirmationScreen(
        onBackClicked = navAdapter::goBack
    )
}