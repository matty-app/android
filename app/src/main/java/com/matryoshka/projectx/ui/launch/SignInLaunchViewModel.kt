package com.matryoshka.projectx.ui.launch

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.isNewUser
import com.matryoshka.projectx.ui.common.userEmail
import com.matryoshka.projectx.ui.common.userName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "SignInLaunchViewModel"

@HiltViewModel
class SignInLaunchViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    var status by mutableStateOf(ScreenStatus.READY)
        private set

    var error: ProjectxException? = null

    suspend fun signInByEmailLink(context: Context): User? {
        return try {
            val email = context.userEmail!!
            val activity = context as Activity
            val link = activity.intent.data.toString()

            if (context.isNewUser) {
                authService.signUpByEmailLink(email, context.userName!!, link)
            } else return authService.signInByEmailLink(email, link)
        } catch (ex: ProjectxException) {
            error = ex
            status = ScreenStatus.ERROR
            null
        } catch (ex: Exception) {
            error = ProjectxException()
            status = ScreenStatus.ERROR
            null
        }
    }
}