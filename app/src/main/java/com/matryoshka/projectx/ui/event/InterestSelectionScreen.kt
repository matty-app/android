package com.matryoshka.projectx.ui.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.matryoshka.projectx.ui.common.ListItem
import com.matryoshka.projectx.ui.common.pickers.DataPickerScreen
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun InterestSelectionScreen(
    state: InterestSelectionState,
    onInit: () -> Unit,
    onInterestClick: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    LaunchedEffect(Unit) {
        onInit()
    }

    DataPickerScreen(
        title = "Select interest",
        onBackClicked = onCancel,
        onSubmit = onSubmit
    ) {
        LazyColumn {
            items(state.interests) { interest ->
                InterestRow(
                    name = interest,
                    onClick = onInterestClick,
                    checked = interest == state.selectedInterest
                )
            }
        }
    }
}

@Composable
private fun InterestRow(name: String, onClick: (String) -> Unit, checked: Boolean) {
    ListItem(
        modifier = Modifier.clickable { onClick(name) },
        text = {
            Text(text = name)
        },
        trailing = if (checked) {
            {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = "Checked")
            }
        } else null
    )
}

@Preview(
    showBackground = true,
)
@Composable
fun InterestSelectionScreenPreview() {
    ProjectxTheme {
        InterestSelectionScreen(
            state = InterestSelectionState(
                loading = false,
                interests = listOf("football", "piano", "books"),
                selectedInterest = "piano"
            ),
            onInit = {},
            onInterestClick = {},
            onSubmit = {},
            onCancel = {}
        )
    }
}
