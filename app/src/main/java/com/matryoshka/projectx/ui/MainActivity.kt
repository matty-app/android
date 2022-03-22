package com.matryoshka.projectx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.navigation.AppNavHost
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navAdapter: NavAdapter

    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = if (isSignInWithEmailLink()) {
            Screen.SIGN_IN_LAUNCH
        } else Screen.LAUNCH

        setContent {
            val navController = rememberNavController()
            ProjectxTheme {
                AppNavHost(
                    startDestination = startDestination,
                    navController = navController,
                    navAdapter = navAdapter
                )
            }
        }
    }

    private fun isSignInWithEmailLink(): Boolean {
        val emailLink = intent.data.toString()
        return authService.isSignInWithEmailLink(emailLink)
    }
}