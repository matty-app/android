package com.matryoshka.projectx.service

import android.util.Log
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.matryoshka.projectx.BuildConfig
import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.data.toProjectxUser
import com.matryoshka.projectx.exception.CheckEmailExistsException
import com.matryoshka.projectx.exception.SendSignInLinkToEmailException
import com.matryoshka.projectx.exception.SignInByEmailLinkException
import com.matryoshka.projectx.exception.SignUpByEmailLinkException
import com.matryoshka.projectx.exception.UpdateUserException
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
            Log.i(TAG, "sendSignInLinkToEmail: sent email to $email")
        } catch (ex: Exception) {
            Log.e(TAG, "sendSignInLinkToEmail $email: ${ex.message}")
            throw SendSignInLinkToEmailException(ex.message)
        }
    }

    override fun isSignInWithEmailLink(link: String) = Firebase.auth.isSignInWithEmailLink(link)

    override suspend fun signInByEmailLink(email: String, link: String): User {
        try {
            val result = Firebase.auth.signInWithEmailLink(email, link).await()
            val user = result.user?.toProjectxUser()
                ?: throw NullPointerException("Firebase.auth.signInWithEmailLink result.user == null")

            Log.i(TAG, "user ${user.uid} signed in")
            return user
        } catch (ex: Exception) {
            Log.e(TAG, "signInByEmailLink $email: ${ex.message}")
            throw SignInByEmailLinkException(ex.message)
        }
    }

    override suspend fun signUpByEmailLink(email: String, name: String, link: String): User {
        try {
            val user = signInByEmailLink(email, link)
            updateUser(user.copy(name = name))
            return user
        } catch (ex: Exception) {
            Log.e(TAG, "signUpByEmailLink $email: ${ex.message}")
            throw SignUpByEmailLinkException(ex.message)
        }
    }

    override suspend fun checkEmailExists(email: String): Boolean {
        try {
            val result = Firebase.auth.fetchSignInMethodsForEmail(email).await()
            return result.signInMethods?.isNotEmpty() ?: true
        } catch (ex: Exception) {
            Log.e(TAG, "checkEmailExists $email: ${ex.message}")
            throw CheckEmailExistsException(ex.message)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return Firebase.auth.currentUser?.toProjectxUser()
    }

    override suspend fun updateUser(user: User) {
        try {
            val firebaseUser = Firebase.auth.currentUser
            if (firebaseUser == null) {
                val message = "User ${user.uid} is signed out"
                Log.e(TAG, "updateUser: $message")
                throw NullPointerException(message)
            }

            val profileUpdates = userProfileChangeRequest {
                displayName = user.name
            }


            firebaseUser.updateProfile(profileUpdates)
        } catch (ex: Exception) {
            Log.e(TAG, "updateUser ${user.uid}: ${ex.message}")
            throw UpdateUserException(ex.message)
        }
    }
}