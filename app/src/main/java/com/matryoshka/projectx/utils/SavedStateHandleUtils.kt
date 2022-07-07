package com.matryoshka.projectx.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle

private const val TAG = "SavedStateHandleUtils"

fun <T> SavedStateHandle.observeOnce(
    lifecycleOwner: LifecycleOwner,
    key: String,
    consumer: (T) -> Unit
) {
    remove<T>(key)
    getLiveData<T>(key).observe(lifecycleOwner) { value ->
        Log.d(TAG, "observeOnce: $value")
        consumer(value)
        remove<T>(key)
    }
}

suspend fun <T> SavedStateHandle.collectOnce(
    key: String,
    initialValue: T,
    consumer: suspend (T) -> Unit
) {
    var isInitialValue = true
    remove<T>(key)
    getStateFlow(key, initialValue).collect { value ->
        if (isInitialValue) {
            isInitialValue = false
        } else {
            Log.d(TAG, "collectOnce: $value")
            consumer(value)
            remove<T>(key)
        }
    }
}