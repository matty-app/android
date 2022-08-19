package com.matryoshka.projectx.ui.email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.navigation.navToEventsFeedScreen
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.service.InvalidVerificationCodeException
import com.matryoshka.projectx.service.MattyApiAuthService
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.XShakerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class EmailConfirmationViewModel @Inject constructor(
    private val authService: AuthService,
    private val apiAuthService: MattyApiAuthService
) : ViewModel() {

    var state by mutableStateOf(
        EmailConfirmationScreenState(
            status = ScreenStatus.READY
        )
    )
        private set


    fun onSendAgainClicked(email: String) {
        viewModelScope.launch {
            changeStatus(ScreenStatus.READY)
            try {
                authService.sendSignInLinkToEmail(email)
            } catch (ex: AppException) {
                setError(ex)
            }
        }
    }

    fun onCodeChanged(
        email: String,
        userName: String? = null,
        navController: NavController
    ) {
        if (state.verificationCodeState.isFull) {
            changeStatus(ScreenStatus.SUBMITTING)
            val code = state.verificationCodeState.toInt()
            viewModelScope.launch {
                try {
                    if (userName == null) {
                        apiAuthService.login(email, code)
                    } else {
                        apiAuthService.register(email, userName, code)
                    }
                    navController.navToEventsFeedScreen()
                } catch (ex: InvalidVerificationCodeException) {
                    changeStatus(ScreenStatus.READY)
                    state.verificationCodeState.clear()
                    state.shakerState.run()
                } catch (ex: AppException) {
                    setError(ex)
                }
            }
        }
    }

    private fun changeStatus(status: ScreenStatus) {
        state = state.copy(status = status)
    }

    private fun setError(error: AppException) {
        state = state.copy(status = ScreenStatus.ERROR, error = error)
    }
}

data class EmailConfirmationScreenState(
    val verificationCodeState: VerificationCodeState = VerificationCodeState(),
    val shakerState: XShakerState = XShakerState(),
    val status: ScreenStatus = ScreenStatus.READY,
    val error: AppException? = null
) {
    val showProgress: Boolean
        get() = status == ScreenStatus.SUBMITTING
    val isErrorToastVisible: Boolean
        get() = status == ScreenStatus.ERROR && error != null
}