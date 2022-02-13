package com.matryoshka.projectx.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ProjectxTypography = Typography(
    h4 = TextStyle(
        color = DarkGray,
        fontSize = 36.sp,
        fontWeight = FontWeight.W400,
    ),
    body1 = TextStyle(
        color = Gray
    ),
    subtitle1 = TextStyle(
        color = Gray
    )
)