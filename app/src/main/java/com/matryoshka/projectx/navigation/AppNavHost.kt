package com.matryoshka.projectx.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

val LocalNavController = compositionLocalOf<NavController> {
    error("CompositionLocal LocalNavController is not present!")
}

@Composable
fun AppNavHost(
    startDestination: String
) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            appNavGraph(navController)
        }
    }
}