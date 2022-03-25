package com.matryoshka.projectx.ui.event.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material.icons.rounded.Subject
import androidx.compose.material.icons.rounded.Title
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.sharedPreferences
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventForm(
    actions: EventFormActions,
    eventState: EventState = EventState()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = eventState.title,
                onValueChange = actions.onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                leadingIcon = {
                    Icon(Icons.Rounded.Title, stringResource(R.string.title_icon))
                },
                placeholder = {
                    Text(text = "Title")
                }
            )

            OutlinedTextField(
                value = eventState.summary,
                onValueChange = actions.onSummaryChange,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Rounded.ShortText, stringResource(R.string.summary_icon))
                },
                trailingIcon = {
                    Icon(Icons.Outlined.Info, stringResource(R.string.info))
                },
                placeholder = {
                    Text(text = stringResource(R.string.summary))
                }
            )

            OutlinedTextField(
                value = eventState.details,
                onValueChange = actions.onDetailsChange,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Rounded.Subject, stringResource(R.string.private_details_icon))
                },
                trailingIcon = {
                    Icon(Icons.Outlined.Info, stringResource(id = R.string.info))
                },
                placeholder = {
                    Text(text = stringResource(R.string.private_details))
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            ListItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Interests,
                        contentDescription = stringResource(R.string.interest_icon)
                    )
                },
                text = {
                    Text(text = eventState.interest ?: stringResource(R.string.select_interest))
                },
                trailing = {
                    Icon(
                        Icons.Outlined.ArrowForwardIos,
                        contentDescription = stringResource(R.string.arrow_forward)
                    )
                }
            )

            Divider()

            ListItem(
                modifier = Modifier.clickable {
                    actions.onLocationClick(context.sharedPreferences)
                },
                icon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = stringResource(id = R.string.location_icon)
                    )
                },
                text = {
                    Text(text = eventState.location ?: stringResource(R.string.location))
                },
                trailing = {
                    Icon(
                        Icons.Outlined.ArrowForwardIos,
                        contentDescription = stringResource(R.string.arrow_forward)
                    )
                }
            )

            Divider()

            ListItem(
                icon = {
                    Icon(
                        Icons.Outlined.Event,
                        contentDescription = ""
                    )
                },
                text = {
                    Text(text = stringResource(R.string.date_time))
                },
                trailing = {
                    Icon(
                        Icons.Outlined.ArrowForwardIos,
                        contentDescription = stringResource(R.string.arrow_forward)
                    )
                }
            )

            Divider()

            ListItem(
                icon = {
                    Icon(
                        Icons.Outlined.PrivacyTip,
                        contentDescription = stringResource(R.string.privacy)
                    )
                },
                text = {
                    Text(text = stringResource(R.string.event_public))
                },
                trailing = {
                    Switch(checked = eventState.public, onCheckedChange = actions.onPublicChange)
                }
            )

            Divider()

            ListItem(
                icon = {
                    Icon(
                        Icons.Outlined.People,
                        contentDescription = stringResource(R.string.people_icon)
                    )
                },
                text = {
                    Text(text = stringResource(R.string.limit_max_participants))
                },
                trailing = {
                    Switch(
                        checked = eventState.limitMaxParticipants,
                        onCheckedChange = actions.onLimitMaxParticipantsChange
                    )
                }
            )

            if (eventState.limitMaxParticipants) {
                OutlinedTextField(
                    value = eventState.maxParticipants?.toString() ?: "",
                    onValueChange = actions.onMaxParticipantsChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = {
                        Text(text = stringResource(R.string.max_participants))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventFormPreview() {
    ProjectxTheme {
        EventForm(
            eventState = EventState(limitMaxParticipants = true),
            actions = EventFormActions()
        )
    }
}