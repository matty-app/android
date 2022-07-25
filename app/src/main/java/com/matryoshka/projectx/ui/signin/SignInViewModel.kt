package com.matryoshka.projectx.ui.signin

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.navigation.navToMailConfirmScreen
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.validator.EmailNotExistsValidator
import com.matryoshka.projectx.ui.validator.EmailValidator
import com.matryoshka.projectx.utils.setIsNewUser
import com.matryoshka.projectx.utils.setUserEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authService: AuthService,
    private val sharedPrefs: SharedPreferences,
) : ViewModel() {

    var state by mutableStateOf(
        SignInScreenState(
            emailField = FieldState(
                initialValue = "",
                validators = listOf(
                    EmailValidator(),
                    EmailNotExistsValidator(authService)
                )
            )
        )
    )
        private set


    private val emailField: FieldState<String>
        get() = state.emailField

    fun onLogInClicked(navController: NavController) {
        viewModelScope.launch {
            changeStatus(ScreenStatus.SUBMITTING)
            try {
                if (validate()) {
                    val email = emailField.value
                    sendLinkToEmail(email)
                    saveSignInPrefs(email)
                    changeStatus(ScreenStatus.READY)
                    navController.navToMailConfirmScreen(email)
                } else {
                    changeStatus(ScreenStatus.READY)
                }
            } catch (ex: ProjectxException) {
                setError(ex)
            }
        }
    }

    private suspend fun validate() = emailField.validate()

    private suspend fun sendLinkToEmail(email: String) {
        authService.sendSignInLinkToEmail(email)
    }

    private fun saveSignInPrefs(email: String) {
        sharedPrefs.setUserEmail(email)
        sharedPrefs.setIsNewUser(false)
    }

    private fun changeStatus(status: ScreenStatus) {
        state = state.copy(status = status)
    }

    private fun setError(error: ProjectxException) {
        state = state.copy(status = ScreenStatus.ERROR, error = error)
    }
}

data class SignInScreenState(
    val emailField: FieldState<String>,
    val status: ScreenStatus = ScreenStatus.READY,
    val error: ProjectxException? = null
) {
    val enabled: Boolean
        get() = status != ScreenStatus.SUBMITTING
    val isProgressIndicatorVisible: Boolean
        get() = status == ScreenStatus.SUBMITTING
    val isErrorToastVisible: Boolean
        get() = status == ScreenStatus.ERROR && error != null
}