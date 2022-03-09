package com.matryoshka.projectx.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class InputField<T>(
    initialValue: T? = null,
    private val validator: Validator<T>? = null,
) {
    var value by mutableStateOf(initialValue)
        private set

    var error by mutableStateOf<FieldError?>(null)
        private set

    val hasError: Boolean
        get() = error != null

    fun onChange(value: T) {
        this.value = value
    }

    fun validate(): Boolean {
        return validator?.let {
            val error = it.validate(value)
            if (error != null) {
                this.error = error
                false
            } else {
                this.error = null
                true
            }
        } ?: true
    }
}