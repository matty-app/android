package com.matryoshka.projectx.navigation

import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.matryoshka.projectx.NavArgument.ARG_EMAIL
import com.matryoshka.projectx.NavArgument.ARG_EVENT
import com.matryoshka.projectx.NavArgument.ARG_EVENT_ID
import com.matryoshka.projectx.NavArgument.ARG_LOCATION
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.data.event.Location
import com.matryoshka.projectx.utils.registerLocalDateTimeAdapter

fun NavController.navToMailConfirmScreen(email: String) {
    navigate("${Screen.EMAIL_CONFIRM}?$ARG_EMAIL=$email")
}

fun NavController.navToEventsFeedScreen() {
    navigate(Screen.EVENTS_FEED_SCREEN)
}

fun NavController.navToEventEditingScreen(event: Event? = null) {
    val route = event?.let {
        val gson = GsonBuilder().registerLocalDateTimeAdapter().create()
        val eventJson = gson.toJson(event)
        "${Screen.EVENT_EDITING_SCREEN}?${ARG_EVENT}=$eventJson"
    } ?: Screen.EVENT_EDITING_SCREEN
    navigate(route)
}

fun NavController.navToEventViewingScreen(eventId: String) {
    navigate("${Screen.EVENT_VIEWING_SCREEN}?$ARG_EVENT_ID=$eventId")
}

fun NavController.navToLocationViewingScreen(location: Location) {
    val locationJson = Gson().toJson(location)
    navigate("${Screen.LOCATION_VIEWING_SCREEN}?${ARG_LOCATION}=$locationJson")
}

fun NavController.navToSignUpScreen() {
    navigate(Screen.SIGN_UP)
}

fun NavController.navToUserProfileScreen() {
    navigate(Screen.USER_PROFILE)
}