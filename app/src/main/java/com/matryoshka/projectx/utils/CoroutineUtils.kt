package com.matryoshka.projectx.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

fun <T> CoroutineScope.debounce(timeout: Duration, action: suspend (T) -> Unit): (T) -> Unit {
    var job: Job? = null
    return {
        job = launch {
            job?.cancel()
            delay(timeout)
            action(it)
        }
    }
}