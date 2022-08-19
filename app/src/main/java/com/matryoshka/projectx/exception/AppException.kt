package com.matryoshka.projectx.exception

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import com.matryoshka.projectx.R

open class AppException : Exception {
    @StringRes
    private var resId: Int = R.string.error_occurred
    private var args: Array<Any>? = null

    constructor(
        @StringRes
        resId: Int = R.string.error_occurred,
        args: Array<Any>? = null
    ) : super() {
        init(resId, args)
    }

    constructor(
        message: String?,
        @StringRes
        resId: Int = R.string.error_occurred,
        args: Array<Any>? = null
    ) : super(message) {
        init(resId, args)
    }

    constructor(
        cause: Throwable?,
        @StringRes
        resId: Int = R.string.error_occurred,
        args: Array<Any>? = null
    ) : super(cause) {
        init(resId, args)
    }

    constructor(
        message: String?,
        cause: Throwable?,
        @StringRes
        resId: Int = R.string.error_occurred,
        args: Array<Any>? = null
    ) : super(message, cause) {
        init(resId, args)
    }

    private fun init(resId: Int, args: Array<Any>?) {
        this.resId = resId
        this.args = args
    }

    @SuppressLint("StringFormatInvalid")
    fun toLocalString(context: Context) = context.getString(resId, args)
}

fun throwException(ex: Exception, logTag: String, methodName: String): Nothing {
    val logMessage = "$methodName: ${ex.message}"
    Log.d(logTag, logMessage)
    throw ex
}