package com.matryoshka.projectx.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val lightColorPalette = lightColors(
    primary = Violet,
    secondary = Blue,
    primaryVariant = PaleBlue
)

@Composable
fun ProjectxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColorPalette,
        typography = ProjectxTypography,
        content = content
    )
}