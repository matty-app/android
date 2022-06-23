package com.matryoshka.projectx.ui.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.data.map.SuggestedLocation
import com.matryoshka.projectx.ui.common.ScreenStatus.READY
import com.matryoshka.projectx.ui.common.textFieldState
import com.matryoshka.projectx.ui.common.withAnyPermissionsGranted
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationSelectionScreen(
    state: LocationChangeState,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    onCancelingSearch: () -> Unit,
    onSuggestionClick: (SuggestedLocation) -> Unit,
    displayUserLocation: (Context) -> Unit
) {
    val context = LocalContext.current
    val displayUserLocationIfGranted = withAnyPermissionsGranted(
        permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    ) {
        displayUserLocation(context)
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Box(Modifier.offset(y = -(48.dp))) {
                CurrentLocationButton(onClick = displayUserLocationIfGranted)
            }
        },
        topBar = {
            MapScreenTopBar(
                onBackClick = onCancel,
                onDoneClick = onSubmit,
                isDoneButtonVisible = state.location != null
            )
        }
    ) {
        if (state.status == READY) {
            MapSearch(
                searchField = state.searchField,
                suggestions = state.suggestions,
                onSuggestionClick = onSuggestionClick,
                onCancelingSearch = onCancelingSearch
            )
            MapView(state.mapState, onInit = {
                val location = state.location
                if (location == null) {
                    displayUserLocationIfGranted()
                } else {
                    state.mapState.setMarker(location.geoData)
                }
            })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LocationSelectionScreenPreview() {
    ProjectxTheme {
        LocationSelectionScreen(
            state = LocationChangeState(
                status = READY,
                searchField = textFieldState(""),
                mapState = MapState(),
                suggestions = listOf(
                    SuggestedLocation("Pushkina", "", ""),
                    SuggestedLocation("Lermontova", "", ""),
                )
            ),
            onSubmit = {},
            onCancel = {},
            onSuggestionClick = {},
            displayUserLocation = {},
            onCancelingSearch = {}
        )
    }
}