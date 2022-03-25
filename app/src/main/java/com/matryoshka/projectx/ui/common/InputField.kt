package com.matryoshka.projectx.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.matryoshka.projectx.ui.validator.Validator
import kotlin.reflect.KProperty

class InputField<T>(
    initialValue: T? = null,
    private val validators: List<Validator<T>> = listOf(),
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

    suspend fun validate(): Boolean {
        this.error = null
        return validators.firstOrNull {
            val error = it.validate(value)
            if (error != null) {
                this.error = error
                true
            } else {
                false
            }
        } == null
    }
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> InputField<T>.getValue(thisObj: Any?, property: KProperty<*>): T? = value

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> InputField<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
    onChange(value)
}