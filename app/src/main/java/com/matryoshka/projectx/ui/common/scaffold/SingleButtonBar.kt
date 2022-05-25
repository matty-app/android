package com.matryoshka.projectx.ui.common.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SingleButtonBar(
    onClick: () -> Unit,
    text: String
) {
    Box(modifier = Modifier.padding(14.dp)) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}