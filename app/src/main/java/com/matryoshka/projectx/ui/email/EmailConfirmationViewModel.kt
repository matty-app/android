package com.matryoshka.projectx.ui.email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailConfirmationViewModel @Inject constructor(private val authService: AuthService) :
    ViewModel() {

    var state by mutableStateOf(EmailConfirmationScreenState())
        private set

    fun onSendAgainClicked(email: String) {
        viewModelScope.launch {
            changeStatus(ScreenStatus.READY)
            try {
                authService.sendSignInLinkToEmail(email)
            } catch (ex: ProjectxException) {
                setError(ScreenStatus.ERROR, ex)
            }
        }
    }

    private fun changeStatus(status: ScreenStatus) {
        state = state.copy(status = status)
    }

    private fun setError(status: ScreenStatus, error: ProjectxException) {
        state = state.copy(status = status, error = error)
    }
}

data class EmailConfirmationScreenState(
    val status: ScreenStatus = ScreenStatus.READY,
    val error: ProjectxException? = null
)