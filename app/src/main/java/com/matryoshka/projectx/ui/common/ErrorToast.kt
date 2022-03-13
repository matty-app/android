package com.matryoshka.projectx.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.matryoshka.projectx.exception.ProjectxException

@Composable
fun ErrorToast(error: ProjectxException) {
    val context = LocalContext.current

    Toast.makeText(
        context,
        error.toLocalString(context),
        Toast.LENGTH_LONG
    ).show()
}