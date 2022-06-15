package com.matryoshka.projectx.ui.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.data.map.SuggestedLocation
import com.matryoshka.projectx.navigation.LocalNavController
import com.matryoshka.projectx.ui.common.textFieldState
import com.matryoshka.projectx.ui.common.withAnyPermissionsGranted
import com.matryoshka.projectx.ui.map.SearchBarState.COLLAPSED
import com.matryoshka.projectx.ui.map.SearchBarState.EXPANDED
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import kotlinx.coroutines.launch

private const val TAG = "LocationSelectionScreen"

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationSelectionScreen(
    state: LocationChangeState,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    onSuggestionClick: (SuggestedLocation) -> Unit,
    displayUserLocation: (Context) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val searchFieldFocusRequester = remember { FocusRequester() }
    val searchBarSwipeableState = rememberSearchBarSwipeableState(searchFieldFocusRequester)
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

    LaunchedEffect(state.mapState.isInitialized) {
        if (state.mapState.isInitialized) {
            displayUserLocationIfGranted()
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = isMapInitialized, enter = fadeIn() + slideInVertically()) {
                SearchBottomBar(
                    fieldFocusRequester = searchFieldFocusRequester,
                    swipeableState = searchBarSwipeableState,
                    searchField = state.searchField,
                    suggestions = state.suggestions,
                    onSuggestionClick = { suggestion ->
                        onSuggestionClick(suggestion)
                        state.searchField.onChange(suggestion.name)
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            searchBarSwipeableState.animateTo(COLLAPSED)
                        }
                    },
                    onMyLocationClick = {
                        displayUserLocationIfGranted()
                    },
                    onBackClick = onCancel,
                    onSubmit = onSubmit
                )
            }
        }
    ) {
        Box {
            MapView(state.mapState, onMapInitialized = {
                isMapInitialized = true
            })
            SearchBarOverlay(
                display = searchBarSwipeableState.isExpanded,
                onTap = {
                    focusManager.clearFocus()
                    searchBarSwipeableState.animateTo(COLLAPSED)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private val SwipeableState<SearchBarState>.isExpanded
    get() = currentValue == EXPANDED

@Preview(showSystemUi = true)
@Composable
fun LocationSelectionScreenPreview() {
    ProjectxTheme {
        CompositionLocalProvider(LocalNavController provides rememberNavController()) {
            LocationSelectionScreen(
                state = LocationChangeState(
                    searchField = textFieldState(""),
                    mapState = MapState()
                ),
                onSubmit = {},
                onCancel = {},
                onSuggestionClick = {},
                displayUserLocation = {}
            )
        }
    }
}
