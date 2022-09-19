package com.matryoshka.projectx.ui.event.editing

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.clearFocusOnTapOut
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.event.editing.form.EventFormActions
import com.matryoshka.projectx.ui.event.editing.form.EventFormView
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventEditingScreen(
    navController: NavController,
    state: EventEditingState,
    eventFormActions: EventFormActions,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.clearFocusOnTapOut(focusManager),
        backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.03f),
        topBar = {
            TopBar(
                title = stringResource(id = R.string.new_event),
                onBackClicked = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(14.dp)) {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.event_submit),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    ) {
        Column(
            Modifier
                .padding(top = 16.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
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
fun EventEditingScreenPreview() {
    ProjectxTheme {
        EventEditingScreen(
            state = EventEditingState(status = ScreenStatus.READY),
            eventFormActions = EventFormActions(),
            navController = rememberNavController(),
            onSubmit = {}
        )
    }
}