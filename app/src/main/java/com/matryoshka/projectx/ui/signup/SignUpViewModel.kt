package com.matryoshka.projectx.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matryoshka.projectx.service.FirebaseAuthService
import com.matryoshka.projectx.ui.common.EmailValidator
import com.matryoshka.projectx.ui.common.InputField
import com.matryoshka.projectx.ui.common.NameValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    val nameField = InputField(validator = NameValidator())
    val emailField = InputField(validator = EmailValidator())

    fun validate(): Boolean {
        val nameValid = nameField.validate()
        val emailValid = emailField.validate()
        //TODO добавить проверку, что email ещё не существует
        return nameValid && emailValid
    }

    fun sendLinkToEmail(email: String) {
        viewModelScope.launch {
            FirebaseAuthService().sendSignInLinkToEmail(email)
        }
    }
}