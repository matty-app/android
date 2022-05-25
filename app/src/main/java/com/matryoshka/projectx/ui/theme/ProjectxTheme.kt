package com.matryoshka.projectx.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightColorPalette = lightColors(
    primary = Violet,
    onPrimary = Color.White,
    secondary = Blue,
    primaryVariant = PaleBlue,
    onBackground = Color(0XFF1C1B1F),
    surface = Color(0XFFFFFBFE),
    onSurface = Color(0XFF1C1B1F),
    error = Color(0XFFB3261E),
    onError = Color(0XFFFFFFFF)
)

@Composable
fun ProjectxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColorPalette,
        typography = ProjectxTypography,
        content = content
    )
}