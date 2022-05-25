package com.matryoshka.projectx.ui.common.pickers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.matryoshka.projectx.ui.common.scaffold.SingleButtonBar
import com.matryoshka.projectx.ui.common.scaffold.TopBar

@Composable
fun DataPickerScreen(
    title: String,
    onBackClicked: () -> Unit,
    onSubmit: () -> Unit,
    buttonText: String = "OK",
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = title, onBackClicked = onBackClicked)
        },
        bottomBar = {
            SingleButtonBar(onClick = onSubmit, text = buttonText)
        },
        content = content
    )
}