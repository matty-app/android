package com.matryoshka.projectx.navigation

import androidx.navigation.NavController
import com.matryoshka.projectx.NavArgument.ARG_EMAIL

fun NavController.navToMailConfirmScreen(email: String) {
    navigate("${Screen.EMAIL_CONFIRM}?$ARG_EMAIL=$email")
}

fun NavController.navToEventsFeedScreen() {
    navigate(Screen.EVENTS_FEED_SCREEN)
}

fun NavController.navToSignUpScreen() {
    navigate(Screen.SIGN_UP)
}

fun NavController.navToUserProfileScreen() {
    navigate(Screen.USER_PROFILE)
}