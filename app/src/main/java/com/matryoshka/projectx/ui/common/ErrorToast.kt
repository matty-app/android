package com.matryoshka.projectx.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.matryoshka.projectx.exception.AppException

@Composable
fun ErrorToast(error: AppException) {
    val context = LocalContext.current

    Toast.makeText(
        context,
        error.toLocalString(context),
        Toast.LENGTH_LONG
    ).show()
}