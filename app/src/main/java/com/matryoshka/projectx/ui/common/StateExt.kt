package com.matryoshka.projectx.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun <T> rememberMutableStateOf(value: T) = remember {
    mutableStateOf(value)
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> StateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> MutableStateFlow<T>.setValue(
    thisObj: Any?,
    property: KProperty<*>,
    value: T
) {
    this.value = value
}
