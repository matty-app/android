package com.matryoshka.projectx.ui.theme

import androidx.compose.material.Typography

private val default = Typography()

val ProjectxTypography = Typography(
    h4 = default.h4.copy(color = DarkGray),
    h5 = default.h5.copy(color = DarkGray),
    body1 = default.body1.copy(color = Gray),
    subtitle1 = default.subtitle1.copy(color = Gray),
    subtitle2 = default.subtitle2.copy(color = Gray)
)