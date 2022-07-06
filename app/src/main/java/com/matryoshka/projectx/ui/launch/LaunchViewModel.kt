package com.matryoshka.projectx.ui.launch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.navToEventsFeedScreen
import com.matryoshka.projectx.navigation.navToSignUpScreen
import com.matryoshka.projectx.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "LaunchViewModel"

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    fun checkUserSignedIn(navController: NavController) {
        if (authService.currentUser == null) {
            Log.d(TAG, "checkUserSignedIn: unauthenticated")
            navController.navToSignUpScreen()
        } else {
            navController.navToEventsFeedScreen()
        }
    }
}