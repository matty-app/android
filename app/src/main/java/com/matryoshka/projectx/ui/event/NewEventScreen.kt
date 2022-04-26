package com.matryoshka.projectx.ui.event

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.common.scaffold.UserProfileButton
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.event.form.EventFormActions
import com.matryoshka.projectx.ui.event.form.EventFormView
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewEventScreen(
    navController: NavController,
    state: NewEventState,
    eventFormActions: EventFormActions,
    onInit: () -> Unit,
    onSubmit: (NavController) -> Unit
) {
    LaunchedEffect(Unit) {
        onInit()
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.03f),
        topBar = {
            TopBar(title = stringResource(id = R.string.new_event)) {
                UserProfileButton(navController)
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(14.dp)) {
                Button(
                    onClick = {
                        onSubmit(navController)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.event_submit),
                        color = Color.White
                    )
                }
            }
        }
    ) {
        Column(Modifier.padding(top = 16.dp)) {
            if (state.showProgress) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (state.displayForm) {
                EventFormView(
                    state = state.formState,
                    actions = eventFormActions,
                    navController = navController
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewEventScreenPreview() {
    ProjectxTheme {
        NewEventScreen(
            state = NewEventState(status = ScreenStatus.READY),
            onInit = {},
            eventFormActions = EventFormActions(),
            navController = rememberNavController(),
            onSubmit = {}
        )
    }
}