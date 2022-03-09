package com.matryoshka.projectx.service

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.matryoshka.projectx.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "FirebaseAuthService"
const val SIGN_UP_URL = "https://appprojectx.page.link/signUpFinish"

class FirebaseAuthService @Inject constructor() : AuthService {

    override suspend fun sendSignInLinkToEmail(email: String) {
        val actionCodeSettings = actionCodeSettings {
            url = SIGN_UP_URL
            handleCodeInApp = true
            setAndroidPackageName(
                BuildConfig.APPLICATION_ID,
                true,
                BuildConfig.VERSION_NAME
            )
        }

        try {
            Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
            Log.i(TAG, "sent email to $email")
        }
        catch (ex: Exception) {
            Log.e(TAG, "error sending authentication link to $email: ${ex.message}", )
        }
    }

    override fun isSignInWithEmailLink(link: String) = Firebase.auth.isSignInWithEmailLink(link)

    override suspend fun signInByEmailLink(email: String, link: String): FirebaseUser? {
        Log.i(TAG, "start sign in with email $email")
        val result = Firebase.auth.signInWithEmailLink(email, link).await()
        val user = result.user

        if (user != null) {
            Log.i(TAG, "user ${user.uid} signed in")
        }
        return user
    }

    override suspend fun checkEmailExists(email: String): Boolean {
        val result = Firebase.auth.fetchSignInMethodsForEmail(email).await()
        return result.signInMethods?.isNotEmpty() ?: true
    }
}