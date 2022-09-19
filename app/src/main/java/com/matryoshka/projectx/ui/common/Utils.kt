package com.matryoshka.projectx.ui.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
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

fun Modifier.clearFocusOnTapOut(focusManager: FocusManager) = pointerInput(Unit) {
    detectTapGestures(
        onTap = { focusManager.clearFocus() }
    )
}