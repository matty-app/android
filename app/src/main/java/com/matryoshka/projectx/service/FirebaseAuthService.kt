package com.matryoshka.projectx.service

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ActionCodeUrl
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.matryoshka.projectx.BuildConfig
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.exception.CheckEmailExistsException
import com.matryoshka.projectx.exception.SendSignInLinkToEmailException
import com.matryoshka.projectx.exception.SignInByEmailLinkException
import com.matryoshka.projectx.exception.SignUpByEmailLinkException
import com.matryoshka.projectx.exception.UpdateUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


const val SIGN_UP_URL = "https://appprojectx.page.link/signUpFinish"
const val CHANGE_EMAIL_URL = "https://appprojectx.page.link/changeEmailFinish"

private const val TAG = "FirebaseAuthService"
private const val NEW_EMAIL_PARAM = "newEmail"

class FirebaseAuthService @Inject constructor(private val auth: FirebaseAuth) : AuthService {
    private val currentFirebaseUser: FirebaseUser?
        get() = auth.currentUser

    override val currentUser: User?
        get() = currentFirebaseUser?.toUser()

    override suspend fun sendSignInLinkToEmail(email: String) {
        try {
            val emailToSend: String
            val actionUrl: String
            if (currentFirebaseUser == null) {
                emailToSend = email
                actionUrl = SIGN_UP_URL
            } else {
                emailToSend = currentUser!!.email!!
                actionUrl = "$CHANGE_EMAIL_URL?newEmail=$email"
            }
            val actionCodeSettings = actionCodeSettings {
                url = actionUrl
                handleCodeInApp = true
                setAndroidPackageName(
                    BuildConfig.APPLICATION_ID,
                    true,
                    BuildConfig.VERSION_NAME
                )
            }
            auth.sendSignInLinkToEmail(emailToSend, actionCodeSettings).await()
            Log.i(TAG, "sendSignInLinkToEmail: sent email to $email")
        } catch (ex: Exception) {
            Log.e(TAG, "sendSignInLinkToEmail $email: ${ex.message}")
            throw SendSignInLinkToEmailException(ex)
        }
    }

    override fun isSignInWithEmailLink(link: String) = auth.isSignInWithEmailLink(link)

    override fun isChangeEmailLink(link: String) = link.contains(CHANGE_EMAIL_URL)

    override suspend fun signInByEmailLink(email: String, link: String): User {
        try {
            val result = auth.signInWithEmailLink(email, link).await()
            val user = result.user?.toUser()
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

    override suspend fun updateEmail(changeEmailLink: String) {
        val firebaseUser = currentFirebaseUser
        try {
            if (firebaseUser == null) {
                throw NullPointerException("User is signed out")
            } else {
                val continueUrl = ActionCodeUrl.parseLink(changeEmailLink)!!.continueUrl
                val uri = Uri.parse(continueUrl)
                val newEmail = uri.getQueryParameter(NEW_EMAIL_PARAM)!!
                val credential =
                    EmailAuthProvider.getCredentialWithLink(firebaseUser.email!!, changeEmailLink)
                firebaseUser.reauthenticateAndRetrieveData(credential).await()
                firebaseUser.updateEmail(newEmail).await()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "updateEmail ${currentFirebaseUser?.uid}: ${ex.message}")
            //TODO("throw exception")
        }
    }
}

private fun FirebaseUser.toUser() = User(
    id = uid,
    name = displayName!!,
    email = email
)