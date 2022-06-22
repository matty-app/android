package com.matryoshka.projectx.ui.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.data.map.SuggestedLocation
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
    var isMapInitialized by remember {
        mutableStateOf(false)
    }
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
        if (isMapInitialized) {
            MapSearch(
                searchField = state.searchField,
                suggestions = state.suggestions,
                onSuggestionClick = onSuggestionClick,
                onCancelingSearch = onCancelingSearch
            )
        }
        MapView(state.mapState, onMapInitialized = {
            isMapInitialized = true
            displayUserLocationIfGranted()
        })
    }
}


@Preview(showSystemUi = true)
@Composable
fun LocationSelectionScreenPreview() {
    ProjectxTheme {
        LocationSelectionScreen(
            state = LocationChangeState(
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