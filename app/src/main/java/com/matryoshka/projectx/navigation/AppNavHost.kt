package com.matryoshka.projectx.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navController: NavHostController,
    navAdapter: NavAdapter
) {
    NavFlowHandler(
        navController = navController,
        navAdapter = navAdapter
    )

    NavHost(
        navController = navController,
        startDestination = Screen.SIGN_UP
    ) {
        appNavGraph(navAdapter)
    }
}