package com.matryoshka.projectx.ui.event

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.sharedPreferences
import com.matryoshka.projectx.ui.event.form.EventForm
import com.matryoshka.projectx.ui.event.form.EventFormActions
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun NewEventScreen(
    state: NewEventUiState,
    eventFormActions: EventFormActions,
    onInit: (SharedPreferences) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onInit(context.sharedPreferences)
    }
    Scaffold(
        bottomBar = {
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.event_submit),
                    color = Color.White
                )
            }
        }
    ) {
        if (state.showProgress) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.displayForm) {
            EventForm(
                eventState = state.event,
                actions = eventFormActions
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    ProjectxTheme {
        NewEventScreen(
            state = NewEventUiState(status = ScreenStatus.READY),
            onInit = {},
            eventFormActions = EventFormActions()
        )
    }
}