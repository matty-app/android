package com.matryoshka.projectx.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.matryoshka.projectx.navigation.AppNavHost
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = getStartScreen()

        setContent {
            ProjectxTheme {
                AppNavHost(startDestination)
            }
        }
    }

    private fun getStartScreen(): String {
        val intentData = intent.data.toString()
        Log.d(TAG, "getStartScreen: $intentData")
        return when {
            authService.isChangeEmailLink(intentData) -> {
                lifecycleScope.launch { authService.updateEmail(intentData) }
                Screen.LAUNCH
            }
            authService.isSignInWithEmailLink(intentData) -> Screen.SIGN_IN_LAUNCH
            else -> Screen.LAUNCH
        }
    }
}