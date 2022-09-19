package com.matryoshka.projectx.ui.common

import androidx.navigation.NavController
import com.matryoshka.projectx.exception.UserSignedOutException
import com.matryoshka.projectx.navigation.navToSignInScreen

suspend fun authorizedAction(navController: NavController, action: suspend () -> Unit) {
    try {
        action()
    } catch (ex: UserSignedOutException) {
        navController.navToSignInScreen()
    }
}