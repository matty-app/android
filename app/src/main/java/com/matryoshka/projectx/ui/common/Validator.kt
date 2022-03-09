package com.matryoshka.projectx.ui.common

import android.util.Patterns
import com.matryoshka.projectx.R

interface Validator<T> {
    fun validate(value: T?): FieldError?
}

class NameValidator : Validator<String> {
    override fun validate(value: String?): FieldError? {
        if (value.isNullOrBlank()) return StringResourceError(R.string.name_required)
        return null
    }
}

class EmailValidator : Validator<String> {
    override fun validate(value: String?): FieldError? {
        if (value.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return StringResourceError(R.string.valid_email_required)
        }
        return null
    }
}