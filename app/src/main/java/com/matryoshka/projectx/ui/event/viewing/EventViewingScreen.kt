package com.matryoshka.projectx.ui.event.viewing

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.event.Location
import com.matryoshka.projectx.ui.common.ErrorToast
import com.matryoshka.projectx.ui.common.ListItem
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.event.EventHeader
import com.matryoshka.projectx.ui.event.eventPreview
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventViewingScreen(
    state: EventViewingState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onLocationClick: (Location) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = "", onBackClicked = onBackClick) {
                if (state.isMine) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "", //TODO
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    ) {
        if (state.isProgressIndicatorVisible) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.isErrorToastVisible) {
            ErrorToast(error = state.error!!)
        }

        if (state.status == ScreenStatus.READY) {
            val event = state.requireEvent
            Column(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                EventHeader(
                    event = event,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                ListItem(modifier = Modifier.heightIn(min = 0.dp)) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.summary),
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = event.summary)
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                ListItem(modifier = Modifier.heightIn(min = 0.dp)) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.private_details),
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = event.details)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ListItem(
                    modifier = Modifier.clickable { onLocationClick(event.location) },
                    text = {
                        Text(
                            text = event.location.name ?: stringResource(R.string.location)
                        )
                    },
                    icon = {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = stringResource(id = R.string.location_icon),
                            tint = MaterialTheme.colors.primary
                        )
                    },
                    trailing = {
                        Icon(
                            Icons.Outlined.ChevronRight,
                            contentDescription = stringResource(R.string.arrow_forward)
                        )
                    }
                )
                EventDateTime(
                    dateTime = event.startDate,
                    icon = {
                        Icon(
                            Icons.Outlined.DateRange,
                            contentDescription = stringResource(R.string.date_time),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                )
                EventDateTime(dateTime = event.endDate)
                if (state.isMine) {
                    ListItem {
                        val context = LocalContext.current
                        val isPublic = remember(event.public) {
                            if (event.public) context.resources.getString(R.string.event_public)
                            else context.resources.getString(R.string.event_private)
                        }

                        Text(text = isPublic)
                    }
                    if (event.withApproval) {
                        ListItem {
                            Text(text = stringResource(id = R.string.event_with_approval))
                        }
                    }
                    if (event.maxParticipants != null) {
                        ListItem(
                            text = { Text(text = stringResource(id = R.string.max_participants)) },
                            trailing = { Text(text = event.maxParticipants.toString()) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDateTime(
    dateTime: LocalDateTime,
    icon: (@Composable () -> Unit)? = null
) {
    val formattedDate = remember(dateTime) {
        dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }
    val formattedTime = remember(dateTime) {
        dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }

    ListItem(
        icon = icon ?: {}, //reserve space
        trailing = {
            Text(text = formattedTime)
        }
    ) {
        Text(text = formattedDate)
    }
}

@Preview(showBackground = true)
@Composable
fun EventViewingScreenPreview() {
    ProjectxTheme {
        EventViewingScreen(
            state = EventViewingState(
                event = eventPreview,
                isMine = true
            ),
            onBackClick = {},
            onEditClick = {},
            onLocationClick = {}
        )
    }
}