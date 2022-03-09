package com.matryoshka.projectx.ui.launch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(
    delayMillis: Long,
    onDelayFinished: () -> Unit
) {
    LaunchScreen {
        delay(delayMillis)
        onDelayFinished()
    }
}

@Composable
fun LaunchScreen(action: suspend () -> Unit) {
    LaunchedEffect(Unit) {
        action()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp)
        )
    }
}