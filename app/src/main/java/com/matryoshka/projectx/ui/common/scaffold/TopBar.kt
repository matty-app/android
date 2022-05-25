package com.matryoshka.projectx.ui.common.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Normal
            )
        },
        navigationIcon = if (onBackClicked != null) {
            {
                Icon(
                    Icons.Outlined.ArrowBackIos,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        onBackClicked()
                    },
                    tint = MaterialTheme.colors.primary
                )
            }
        } else null,
        actions = actions
    )
}