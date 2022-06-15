package com.matryoshka.projectx.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.map.SuggestedLocation
import com.matryoshka.projectx.ui.common.FieldState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBottomBar(
    fieldFocusRequester: FocusRequester,
    swipeableState: SwipeableState<SearchBarState>,
    searchField: FieldState<String>,
    suggestions: List<SuggestedLocation>,
    onSuggestionClick: (SuggestedLocation) -> Unit,
    onBackClick: () -> Unit,
    onMyLocationClick: () -> Unit,
    onSubmit: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        Modifier.zIndex(3f)
    ) {
        val maxHeight = constraints.maxHeight.toFloat()
        val density = LocalDensity.current.density
        val peekHeightPx = (148 * density)
        val currentBoxHeightDp = (swipeableState.offset.value / density).dp
        val isButtonsVisible = swipeableState.offset.value < peekHeightPx * 1.1
        Column {
            Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 18.dp)) {
                GoBackButton(onClick = onBackClick)
                if (isButtonsVisible) {
                    MyLocationButton(onClick = onMyLocationClick)
                }
            }
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colors.background)
                    .padding(top = 14.dp, end = 14.dp, start = 14.dp)
                    .background(MaterialTheme.colors.background)
                    .swipeable(
                        state = swipeableState,
                        orientation = Orientation.Vertical,
                        anchors = mapOf(
                            maxHeight * .6f to SearchBarState.EXPANDED,
                            peekHeightPx to SearchBarState.COLLAPSED
                        ),
                        reverseDirection = true
                    )
                    .height(currentBoxHeightDp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = searchField.value,
                    onValueChange = searchField::onChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(fieldFocusRequester)
                        .onFocusChanged {
                            coroutineScope.launch {
                                if (it.isFocused) {
                                    swipeableState.animateTo(SearchBarState.EXPANDED)
                                }
                            }
                        },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.search_text)
                        )
                    },
                    trailingIcon = {
                        if (searchField.value.isNotEmpty()) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_text),
                                modifier = Modifier.clickable {
                                    searchField.onChange("")
                                }
                            )
                        }
                    },
                    singleLine = true
                )
                if (!isButtonsVisible) {
                    LazyColumn {
                        items(suggestions) {
                            SuggestionItem(location = it, onClick = onSuggestionClick)
                        }
                    }
                }
                if (isButtonsVisible) {
                    Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.ok_text),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberSearchBarSwipeableState(
    searchFieldFocusRequester: FocusRequester,
    focusManager: FocusManager = LocalFocusManager.current
) = rememberSwipeableState(
    initialValue = SearchBarState.COLLAPSED,
    confirmStateChange = {
        when (it) {
            SearchBarState.COLLAPSED -> focusManager.clearFocus()
            SearchBarState.EXPANDED -> searchFieldFocusRequester.requestFocus()
        }
        true
    })

@Composable
fun SearchBarOverlay(display: Boolean = false, onTap: suspend () -> Unit) {
    if (display) {
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .background(Color.White.copy(alpha = .1f))
                .pointerInput(Unit) {
                    detectTapGestures {
                        coroutineScope.launch {
                            onTap()
                        }
                    }
                }
        )
    }
}

@Composable
private fun RowScope.MyLocationButton(onClick: () -> Unit) {
    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
        CircleButton(onClick) {
            Icon(
                Icons.Default.MyLocation,
                contentDescription = stringResource(R.string.current_location),
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colors.onBackground.copy(alpha = .9f)
            )
        }
    }
}

@Composable
private fun GoBackButton(onClick: () -> Unit) {
    CircleButton(onClick = onClick) {
        Icon(
            Icons.Outlined.KeyboardArrowLeft,
            contentDescription = stringResource(R.string.arrow_back),
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colors.onBackground.copy(alpha = .9f)
        )
    }
}

@Composable
private fun CircleButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

enum class SearchBarState {
    COLLAPSED,
    EXPANDED
}