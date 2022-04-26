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