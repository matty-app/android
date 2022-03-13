package com.matryoshka.projectx.ui.validator

import android.util.Patterns
import com.matryoshka.projectx.R
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.FieldError
import com.matryoshka.projectx.ui.common.StringResourceError

class EmailValidator : Validator<String> {
    override suspend fun validate(value: String?): FieldError? {
        if (value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(value).matches())
            return StringResourceError(R.string.valid_email_required)

        return null
    }
}

class EmailExistsValidator(private val authService: AuthService) : Validator<String> {
    override suspend fun validate(value: String?): FieldError? {
        if (value != null && authService.checkEmailExists(value))
            return StringResourceError(R.string.email_exists)

        return null
    }
}

class EmailNotExistsValidator(private val authService: AuthService) : Validator<String> {
    override suspend fun validate(value: String?): FieldError? {
        if (value != null && !authService.checkEmailExists(value))
            return StringResourceError(R.string.email_not_exists)

        return null
    }
}