package com.matryoshka.projectx.navigation

import androidx.navigation.NavOptionsBuilder
import com.matryoshka.projectx.navigation.NavigationEvent.GoBack
import com.matryoshka.projectx.navigation.NavigationEvent.NavigateToRoute
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavAdapter @Inject constructor() {
    private val _navigationFlow = MutableSharedFlow<NavigationEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val navigationFlow = _navigationFlow.asSharedFlow()

    fun goBack() {
        _navigationFlow.tryEmit(GoBack)
    }

    fun navigateTo(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
        _navigationFlow.tryEmit(
            NavigateToRoute(route, builder)
        )
    }

    fun goToEmailConfirmationScreen(email: String) {
        navigateTo(
            route = "${Screen.EMAIL_CONFIRM}?$ARG_EMAIL=$email"
        )
    }
}

sealed class NavigationEvent {
    object GoBack : NavigationEvent() {
        override fun toString(): String {
            return "Go back"
        }
    }

    class NavigateToRoute(
        val route: String,
        val builder: NavOptionsBuilder.() -> Unit
    ) : NavigationEvent() {
        override fun toString(): String {
            return "Go to route - $route"
        }
    }
}