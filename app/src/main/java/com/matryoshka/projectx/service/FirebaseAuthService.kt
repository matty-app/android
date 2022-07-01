package com.matryoshka.projectx.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.matryoshka.projectx.BuildConfig
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.data.user.toProjectxUser
import com.matryoshka.projectx.exception.CheckEmailExistsException
import com.matryoshka.projectx.exception.SendSignInLinkToEmailException
import com.matryoshka.projectx.exception.SignInByEmailLinkException
import com.matryoshka.projectx.exception.SignUpByEmailLinkException
import com.matryoshka.projectx.exception.UpdateUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "FirebaseAuthService"
const val SIGN_UP_URL = "https://appprojectx.page.link/signUpFinish"

class FirebaseAuthService @Inject constructor(private val auth: FirebaseAuth) : AuthService {

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
            auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
            Log.i(TAG, "sendSignInLinkToEmail: sent email to $email")
        } catch (ex: Exception) {
            Log.e(TAG, "sendSignInLinkToEmail $email: ${ex.message}")
            throw SendSignInLinkToEmailException(ex)
        }
    }

    override fun isSignInWithEmailLink(link: String) = auth.isSignInWithEmailLink(link)

    override suspend fun signInByEmailLink(email: String, link: String): User {
        try {
            val result = auth.signInWithEmailLink(email, link).await()
            val user = result.user?.toProjectxUser()
                ?: throw NullPointerException("auth.signInWithEmailLink result.user == null")

            Log.i(TAG, "user ${user.id} signed in")
            return user
        } catch (ex: Exception) {
            Log.e(TAG, "signInByEmailLink $email: ${ex.message}")
            throw SignInByEmailLinkException(ex)
        }
    }

    override suspend fun signUpByEmailLink(email: String, name: String, link: String): User {
        try {
            val user = signInByEmailLink(email, link).copy(name = name)
            updateUser(user)
            return user
        } catch (ex: Exception) {
            Log.e(TAG, "signUpByEmailLink $email: ${ex.message}")
            throw SignUpByEmailLinkException(ex)
        }
    }

    override suspend fun checkEmailExists(email: String): Boolean {
        try {
            val result = auth.fetchSignInMethodsForEmail(email).await()
            return result.signInMethods?.isNotEmpty() ?: true
        } catch (ex: Exception) {
            Log.e(TAG, "checkEmailExists $email: ${ex.message}")
            throw CheckEmailExistsException(ex)
        }
    }

    override fun getCurrentUser(): User? {
        return auth.currentUser?.toProjectxUser()
    }

    override suspend fun updateUser(user: User) {
        try {
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                val message = "User ${user.id} is signed out"
                Log.e(TAG, "updateUser: $message")
                throw NullPointerException(message)
            }

            val profileUpdates = userProfileChangeRequest { displayName = user.name }
            firebaseUser.updateProfile(profileUpdates).await()
        } catch (ex: Exception) {
            Log.e(TAG, "updateUser ${user.id}: ${ex.message}")
            throw UpdateUserException(ex)
        }
    }
}