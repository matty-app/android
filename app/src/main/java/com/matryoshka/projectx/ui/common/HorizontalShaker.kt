package com.matryoshka.projectx.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalShaker(
    state: HorizontalShakerState,
    content: @Composable () -> Unit
) {
    val xOffset = remember { Animatable(initialValue = 0.0f) }

    LaunchedEffect(state.shouldShake) {
        if (state.shouldShake) {
            xOffset.animateTo(
                targetValue = 16f,
                animationSpec = tween(durationMillis = 60)
            )
            xOffset.animateTo(
                targetValue = 0.0F,
                animationSpec = spring(
                    dampingRatio = 0.1f,
                    stiffness = Spring.StiffnessMedium
                )
            )
            state.onFinish()
        }
    }

    Box(
        modifier = Modifier.offset(x = xOffset.value.dp)
    ) {
        content()
    }
}

@Stable
class HorizontalShakerState {
    var shouldShake by mutableStateOf(false)
        private set

    fun run() {
        shouldShake = true
    }

    fun onFinish() {
        shouldShake = false
    }
}