package com.matryoshka.projectx.ui.event.editing.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        val focusManager = LocalFocusManager.current

        OutlinedTextFieldSm(
            value = state.nameField.value,
            onValueChange = state.nameField::onChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Outlined.Info, stringResource(R.string.info))
            },
            label = {
                Text(text = "Name")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { focusManager.moveFocus(FocusDirection.Next) })
        )
        OutlinedTextFieldSm(
            value = state.summaryField.value,
            onValueChange = state.summaryField::onChange,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Outlined.Info, stringResource(R.string.info))
            },
            label = {
                Text(text = stringResource(R.string.summary))
            }
        )
        OutlinedTextFieldSm(
            value = state.detailsField.value,
            onValueChange = state.detailsField::onChange,
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
                actions.onInterestClick(navController)
            },
            text = {
                Text(
                    text = state.interestField.value?.name
                        ?: stringResource(R.string.select_interest)
                )
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
                actions.onLocationClick(navController)
            },
            text = {
                Text(
                    text = state.locationField.value?.displayName
                        ?: stringResource(R.string.location)
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
        EventDuration(
            startDateField = state.startDateField,
            endDateField = state.endDateField
        )
        ListItem(
            text = {
                Text(text = stringResource(R.string.event_public))
            },
            trailing = {
                Switch(
                    checked = state.isPublicField.value,
                    onCheckedChange = state.isPublicField::onChange
                )
            }
        )
        ListItem(
            text = {
                Text(text = stringResource(R.string.event_with_approval))
            },
            trailing = {
                Switch(
                    checked = state.withApprovalField.value,
                    onCheckedChange = state.withApprovalField::onChange
                )
            }
        )
        ListItem(
            text = {
                Text(text = stringResource(R.string.limit_max_participants))
            },
            trailing = {
                Switch(
                    checked = state.limitMaxParticipantsField.value,
                    onCheckedChange = state.limitMaxParticipantsField::onChange
                )
            }
        )
        if (state.limitMaxParticipantsField.value) {
            TextField(
                value = state.maxParticipantsField.value ?: "",
                onValueChange = state.maxParticipantsField::onChange,
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