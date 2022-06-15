package com.matryoshka.projectx.ui.map

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.DefaultDisplacementThreshold
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.ui.theme.ProjectxTheme

private val MARKER_SIZE = 80.dp

private const val PICKED_Y_OFFSET_RATIO = .25f
private const val SEARCHING_Y_OFFSET_RATIO_MIN = .05f
private const val SEARCHING_Y_OFFSET_RATIO_MAX = .1f

private const val SEARCHING_SHADOW_WIDTH_RATIO = 2.5f
private const val PICKED_SHADOW_WIDTH_RATIO = 4f

@Composable
fun SearchingMarker(
    isSearching: Boolean,
    disableInitAnimation: Boolean = false
) {
    val animSpec = remember {
        spring(
            dampingRatio = DampingRatioNoBouncy,
            stiffness = StiffnessLow,
            visibilityThreshold = DefaultDisplacementThreshold,
        )
    }
    val yOffsetRatio = remember {
        Animatable(if (disableInitAnimation) PICKED_Y_OFFSET_RATIO else 0f)
    }
    val shadowWidthRatio by animateFloatAsState(
        targetValue = if (isSearching) SEARCHING_SHADOW_WIDTH_RATIO else PICKED_SHADOW_WIDTH_RATIO,
        animSpec
    )
    val shadowGradient = remember(isSearching) {
        if (isSearching) {
            listOf(
                Color.Black,
                Color.Gray,
                Color.LightGray
            )
        } else {
            listOf(
                Color.LightGray,
                Color.Gray,
                Color.White
            )
        }
    }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            yOffsetRatio.animateTo(SEARCHING_Y_OFFSET_RATIO_MIN, animSpec)
            yOffsetRatio.animateTo(
                targetValue = SEARCHING_Y_OFFSET_RATIO_MAX,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            yOffsetRatio.animateTo(.25f, animSpec)
        }
    }

    Canvas(
        Modifier
            .size(MARKER_SIZE)
            .offset(y = (-(MARKER_SIZE/2))),
        onDraw = {
            val lineWidth = size.width * .04f
            val lineHeight = size.height * .25f
            val shadowWidth = lineWidth * shadowWidthRatio
            val yOffset = yOffsetRatio.value * size.height
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        Color(0xFFe53935),
                        Color(0XFFe35d5b)
                    )
                ),
                radius = size.width / 4,
                center = Offset(x = size.width / 2, y = (size.width / 4) + yOffset)
            )
            drawCircle(
                color = Color(0xFFF0F0F0),
                radius = size.width / 14,
                center = Offset(x = size.width * .4f, y = size.width / 6 + yOffset)
            )
            drawOval(
                brush = Brush.linearGradient(
                    colors = shadowGradient
                ),
                topLeft = Offset(x = size.width / 2 - shadowWidth / 2, y = size.height * .98f),
                size = Size(width = shadowWidth, height = shadowWidth / 2)
            )
            drawLine(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0XFFe35d5b),
                        Color(0xFFe53935)
                    )
                ),
                start = Offset(x = size.width / 2, y = size.height / 2 + yOffset),
                end = Offset(x = size.width / 2, y = size.height / 2 + lineHeight + yOffset),
                strokeWidth = lineWidth,
                cap = StrokeCap.Round
            )
        })
}

@Preview(showSystemUi = true)
@Composable
fun PickLocationMarkerPreview() {
    var isSearching by remember {
        mutableStateOf(false)
    }
    ProjectxTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Searching..", style = MaterialTheme.typography.h5)
            SearchingMarker(true)
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Text(text = "Picked..", style = MaterialTheme.typography.h5)
            SearchingMarker(false, disableInitAnimation = true)
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Text(text = "Live Preview", style = MaterialTheme.typography.h5)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Searching")
                Switch(checked = isSearching, onCheckedChange = { isSearching = it })
            }
            Spacer(Modifier.height(24.dp))
            SearchingMarker(isSearching)
        }
    }
}