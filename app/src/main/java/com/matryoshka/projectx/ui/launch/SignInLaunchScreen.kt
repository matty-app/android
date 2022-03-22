package com.matryoshka.projectx.ui.launch

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.matryoshka.projectx.exception.ProjectxException
import com.matryoshka.projectx.ui.common.ScreenStatus

@Composable
fun SignInLaunchScreen(
    status: ScreenStatus,
    error: ProjectxException?,
    signIn: suspend () -> Unit
) {
    val context = LocalContext.current

    if (status == ScreenStatus.ERROR && error != null) {
        Text(
            text = error.toLocalString(context),
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        LaunchScreen {
            signIn()
        }
    }
}