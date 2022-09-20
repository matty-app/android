package com.matryoshka.projectx.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.map.SuggestedLocation
import com.matryoshka.projectx.ui.common.TextFieldState
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapSearch(
    searchField: TextFieldState,
    suggestions: List<SuggestedLocation>,
    onSuggestionClick: (SuggestedLocation) -> Unit,
    onCancelingSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var displayOverlay by remember { mutableStateOf(false) }

    Column(
        Modifier
            .zIndex(3f)
            .padding(12.dp)
    ) {
        TextField(
            value = searchField.value,
            onValueChange = searchField::onChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    displayOverlay = it.isFocused
                }
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            singleLine = true,
            placeholder = {
                Text(text = stringResource(id = R.string.map_search_field_ps))
            },
            trailingIcon = {
                if (searchField.value.isNotEmpty()) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Clear",
                        Modifier.clickable {
                            searchField.onChange("")
                            focusRequester.requestFocus()
                        })
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.body2
        )

        LazyColumn(
            modifier = Modifier.offset(y = 6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(suggestions) {
                Suggestion(suggestion = it, onClick = { suggestion ->
                    focusManager.clearFocus()
                    onSuggestionClick(suggestion)
                })
            }
        }
    }
    Overlay(displayOverlay, onTap = {
        focusManager.clearFocus()
        onCancelingSearch()
        displayOverlay = false
    })
}

@Composable
private fun Overlay(display: Boolean = false, onTap: suspend () -> Unit) {
    if (display) {
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .background(Color.Gray.copy(alpha = .3f))
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
private fun Suggestion(
    suggestion: SuggestedLocation,
    onClick: (SuggestedLocation) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick(suggestion)
            }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colors.background)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(46.dp), contentAlignment = Alignment.Center) {
            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = "",
                tint = MaterialTheme.colors.onBackground.copy(.8f)
            )
        }
        Column {
            Text(text = suggestion.name, style = MaterialTheme.typography.body2)
            Text(text = suggestion.description, style = MaterialTheme.typography.caption)
        }
    }
}