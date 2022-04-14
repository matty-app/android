package com.matryoshka.projectx.support

import com.google.android.gms.tasks.Task
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

fun Any.callPrivateFunction(
    functionName: String,
    args: Array<Any?> = emptyArray()
): Any? {
    this::class.memberFunctions.forEach {
        if (it.name == functionName) {
            try {
                it.isAccessible = true
                return it.call(this, *args)
            } catch (ex: IllegalArgumentException) {
            }
        }
    }
    return null
}

suspend fun Any.callPrivateSuspendFunction(
    functionName: String,
    args: Array<Any?> = emptyArray()
): Any? {
    this::class.memberFunctions.forEach {
        if (it.name == functionName) {
            try {
                it.isAccessible = true
                return it.callSuspend(this, *args)
            } catch (ex: IllegalArgumentException) {
            }
        }
    }
    return null
}

inline fun <T, reified R : Iterable<T>> mockIterable(iterable: Iterable<T>) = mockk<R>().apply {
    every { iterator() } returns iterable.iterator()
}

fun <T> taskMock(resultValue: T, excep: Exception? = null): Task<T> {
    return mockk<Task<T>>().apply {
        every { isComplete } returns true
        every { isCanceled } returns false
        every { exception } returns excep
        every { result } returns resultValue
    }
}

val voidObject: Void? //kostyl for java Void instance creation
    get() = null