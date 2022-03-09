package com.matryoshka.projectx.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matryoshka.projectx.service.FirebaseAuthService
import com.matryoshka.projectx.ui.common.EmailValidator
import com.matryoshka.projectx.ui.common.InputField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    val emailField = InputField(validator = EmailValidator())

    fun validate() = emailField.validate()

    fun sendLinkToEmail(email: String) {
        viewModelScope.launch {
            FirebaseAuthService().sendSignInLinkToEmail(email)
        }
    }
}