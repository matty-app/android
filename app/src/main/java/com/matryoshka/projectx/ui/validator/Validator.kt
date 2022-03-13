package com.matryoshka.projectx.ui.validator

import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.FieldError
import com.matryoshka.projectx.ui.common.StringResourceError

interface Validator<T> {
    suspend fun validate(value: T?): FieldError?
}

class NameValidator : Validator<String> {
    override suspend fun validate(value: String?): FieldError? {
        if (value.isNullOrBlank()) return StringResourceError(R.string.name_required)
        return null
    }
}