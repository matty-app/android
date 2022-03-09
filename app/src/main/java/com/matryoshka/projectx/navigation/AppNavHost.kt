package com.matryoshka.projectx.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navController: NavHostController,
    navAdapter: NavAdapter,
    startDestination: String = Screen.LAUNCH
) {
    NavFlowHandler(
        navController = navController,
        navAdapter = navAdapter
    )

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        appNavGraph(navAdapter)
    }
}