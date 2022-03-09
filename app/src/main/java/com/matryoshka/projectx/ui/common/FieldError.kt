package com.matryoshka.projectx.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FieldError(inputField: InputField<*>) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .padding(start = 16.dp, end = 12.dp)
    ) {
        if (inputField.hasError) {
            Text(
                text = inputField.error?.toString(context) ?: "",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

sealed interface FieldError {
    fun toString(context: Context): String
}

class RawStringError(val text: String) : FieldError {
    override fun toString(context: Context) = text
}

class StringResourceError(
    @StringRes val resId: Int,
    val args: Array<Any>? = null
) : FieldError {
    override fun toString(context: Context) = context.getString(resId, args)
}