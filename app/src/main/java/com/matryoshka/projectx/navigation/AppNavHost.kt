package com.matryoshka.projectx.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    startDestination: String
) {
    val navController = rememberAnimatedNavController()

    //use AnimatedNavHost to disable blinking in multi-scaffold app
    //@see https://stackoverflow.com/questions/68633717/topappbar-flashing-when-navigating-with-compose-navigation
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        ) {
        appNavGraph(navController)
    }
}