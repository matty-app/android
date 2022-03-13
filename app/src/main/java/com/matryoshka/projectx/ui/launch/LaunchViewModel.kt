package com.matryoshka.projectx.ui.launch

import androidx.lifecycle.ViewModel
import com.matryoshka.projectx.navigation.NavAdapter
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val authService: AuthService,
    private val navAdapter: NavAdapter
) : ViewModel() {

    suspend fun checkUserSignedIn() {
        if (authService.getCurrentUser() == null) {
            navAdapter.navigateTo(Screen.SIGN_UP)
        } else {
            navAdapter.navigateTo(Screen.INTERESTS)
        }
    }
}