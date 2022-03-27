package com.matryoshka.projectx.ui.launch

import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.data.repository.UsersRepository
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
    private val authService: AuthService,
    private val usersRepository: UsersRepository,
    private val sharedPrefs: SharedPreferences
) : ViewModel() {

    var status by mutableStateOf(ScreenStatus.READY)
        private set

    var error: ProjectxException? = null

    suspend fun signInByEmailLink(intent: Intent): User? {
        return try {
            val email = sharedPrefs.userEmail!!
            val link = intent.data.toString()
            if (sharedPrefs.isNewUser) {
                val user = authService.signUpByEmailLink(email, sharedPrefs.userName!!, link)
                usersRepository.save(user)
                user
            } else return authService.signInByEmailLink(email, link)
        } catch (ex: ProjectxException) {
            setErrorState(ex)
            null
        } catch (ex: Exception) {
            setErrorState(ProjectxException())
            null
        }
    }

    private fun setErrorState(error: ProjectxException) {
        this.status = ScreenStatus.ERROR
        this.error = error
    }
}