package com.matryoshka.projectx.exception

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import com.matryoshka.projectx.R

open class ProjectxException(
    message: String? = "Error occurred",
    @StringRes private val resId: Int = R.string.error_occurred,
    private val args: Array<Any>? = null
) : Exception(message) {

    @SuppressLint("StringFormatInvalid")
    fun toLocalString(context: Context) = context.getString(resId, args)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ProjectxException)
            return false
        return message == other.message &&
                resId == other.resId &&
                args contentDeepEquals other.args
    }
}