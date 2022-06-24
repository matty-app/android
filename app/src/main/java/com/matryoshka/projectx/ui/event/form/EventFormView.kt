package com.matryoshka.projectx.ui.event.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ListItem
import com.matryoshka.projectx.ui.common.OutlinedTextFieldSm
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun EventFormView(
    actions: EventFormActions,
    state: EventFormState = EventFormState(),
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextFieldSm(
            value = state.name.value,
            onValueChange = state.name::onChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Outlined.Info, stringResource(R.string.info))
            },
            label = {
                Text(text = "Name")
            }
        )
        OutlinedTextFieldSm(
            value = state.summary.value,
            onValueChange = state.summary::onChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Outlined.Info, stringResource(R.string.info))
            },
            label = {
                Text(text = stringResource(R.string.summary))
            }
        )
        OutlinedTextFieldSm(
            value = state.details.value,
            onValueChange = state.details::onChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Outlined.Info, stringResource(id = R.string.info))
            },
            label = {
                Text(text = stringResource(R.string.private_details))
            }
        )
        Spacer(modifier = Modifier.height(6.dp))
        ListItem(
            modifier = Modifier.clickable {
                actions.onInterestClick(navController, lifecycleOwner)
            },
            text = {
                Text(text = state.interest.value ?: stringResource(R.string.select_interest))
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Interests,
                    contentDescription = stringResource(R.string.interest_icon),
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
        ListItem(
            modifier = Modifier.clickable {
                actions.onLocationClick(navController, lifecycleOwner)
            },
            text = {
                Text(text = state.location.value?.displayName ?: stringResource(R.string.location))
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
        EventDateTimeField(
            dateField = state.date,
            timeField = state.time
        )
        ListItem(
            text = {
                Text(text = stringResource(R.string.event_public))
            },
            trailing = {
                Switch(
                    checked = state.public.value,
                    onCheckedChange = state.public::onChange
                )
            }
        )
        ListItem(
            text = {
                Text(text = stringResource(R.string.limit_max_participants))
            },
            trailing = {
                Switch(
                    checked = state.limitMaxParticipants.value,
                    onCheckedChange = state.limitMaxParticipants::onChange
                )
            }
        )
        if (state.limitMaxParticipants.value) {
            TextField(
                value = state.maxParticipants.value ?: "",
                onValueChange = state.maxParticipants::onChange,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                placeholder = {
                    Text(text = stringResource(R.string.max_participants))
                },
                colors = textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(0.12f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventFormPreview() {
    ProjectxTheme {
        EventFormView(
            state = EventFormState(
                limitMaxParticipants = true
            ),
            actions = EventFormActions(),
            navController = rememberNavController()
        )
    }
}