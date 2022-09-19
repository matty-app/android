package com.matryoshka.projectx.ui.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.navigation.navToMailConfirmScreen
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.validator.EmailValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    var state by mutableStateOf(
        SignInScreenState(
            emailField = FieldState(
                initialValue = "",
                validators = listOf(
                    EmailValidator()
                    //EmailNotExistsValidator(authService)
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
                    sendLoginCodeToEmail(email)
                    changeStatus(ScreenStatus.READY)
                    navController.navToMailConfirmScreen(email = email)
                } else {
                    changeStatus(ScreenStatus.READY)
                }
            } catch (ex: AppException) {
                setError(ex)
            }
        }
    }

    private suspend fun validate() = emailField.validate()

    private suspend fun sendLoginCodeToEmail(email: String) =
        authService.sendLoginCode(email)

    private fun changeStatus(status: ScreenStatus) {
        state = state.copy(status = status)
    }

    private fun setError(error: AppException) {
        state = state.copy(status = ScreenStatus.ERROR, error = error)
    }
}

data class SignInScreenState(
    val emailField: FieldState<String>,
    val status: ScreenStatus = ScreenStatus.READY,
    val error: AppException? = null
) {
    val enabled: Boolean
        get() = status != ScreenStatus.SUBMITTING
    val isProgressIndicatorVisible: Boolean
        get() = status == ScreenStatus.SUBMITTING
    val isErrorToastVisible: Boolean
        get() = status == ScreenStatus.ERROR && error != null
}