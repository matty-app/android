package com.matryoshka.projectx.ui.launch

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.matryoshka.projectx.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "SignInLaunchViewModel"

@HiltViewModel
class SignInLaunchViewModel @Inject constructor(private val authService: AuthService) :
    ViewModel() {

    suspend fun signInByEmailLink(email: String, link: String): FirebaseUser? {
        return try {
            authService.signInByEmailLink(email, link)
        } catch (ex: Exception) {
            Log.e(TAG, "signInByEmailLink error: ${ex.message}")
            null
        }
    }
}