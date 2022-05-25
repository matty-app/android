package com.matryoshka.projectx.navigation

import androidx.navigation.NavController
import com.matryoshka.projectx.NavArgument

fun NavController.navToMailConfirmScreen(email: String) {
    navigate("${Screen.EMAIL_CONFIRM}?${NavArgument.ARG_EMAIL}=$email")
}

fun NavController.navToEventsFeedScreen() {
    navigate(Screen.EVENTS_FEED_SCREEN)
}

fun NavController.navToSignUpScreen() {
    navigate(Screen.SIGN_UP)
}