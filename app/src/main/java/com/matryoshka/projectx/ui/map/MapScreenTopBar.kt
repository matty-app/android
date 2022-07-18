package com.matryoshka.projectx.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun MapScreenTopBar(
    onBackClick: () -> Unit,
    onDoneClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 6.dp, vertical = 10.dp)
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            modifier = Modifier
                .clip(CircleShape)
                .padding(4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = onBackClick
                ),
            contentDescription = stringResource(R.string.arrow_back),
            tint = MaterialTheme.colors.onPrimary.copy(alpha = .8f)
        )
        Box(
            Modifier
                .weight(1f), contentAlignment = Alignment.CenterEnd
        ) {
            onDoneClick?.let {
                Icon(
                    Icons.Rounded.Done,
                    contentDescription = stringResource(R.string.ok_text),
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                            onClick = onDoneClick
                        ),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun MapScreenTopBarPreview() {
    ProjectxTheme {
        MapScreenTopBar(onBackClick = { }, onDoneClick = { })
    }
}