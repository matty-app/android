package com.matryoshka.projectx.ui.event.feed

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ErrorToast
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.event.eventListPreview
import com.matryoshka.projectx.ui.theme.ProjectxTheme

private const val BOTTOM_BLUR_HEIGHT = 50

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventsFeedScreen(
    state: EventsFeedState,
    bottomBar: @Composable () -> Unit,
    onAddEventClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit
) {
    Scaffold(
        topBar = { TopBar(onAddEventClick) },
        bottomBar = { BottomBar(bottomBar) }
    ) {
        val error = state.error

        if (state.isProgressIndicatorVisible) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.isErrorToastVisible) {
            ErrorToast(error = error!!)
        }

        if (state.enabled) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(state.events) {
                        EventsFeedItem(
                            event = it,
                            onClick = { onEventClick(it.requireId) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height((BOTTOM_BLUR_HEIGHT * 2).dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(onClick: () -> Unit) {
    TopBar(title = stringResource(id = R.string.upcoming)) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Outlined.AddBox,
                contentDescription = "", //TODO
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun BottomBar(navigationBottomBar: @Composable () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BOTTOM_BLUR_HEIGHT.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colors.background
                        )
                    )
                )
        )
        navigationBottomBar()
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsFeedScreenPreview() {
    ProjectxTheme {
        EventsFeedScreen(
            state = EventsFeedState(events = eventListPreview),
            bottomBar = {},
            onAddEventClick = {},
            onEventClick = {}
        )
    }
}