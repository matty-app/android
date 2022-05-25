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
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.LocalNavController
import com.matryoshka.projectx.ui.common.ListItem
import com.matryoshka.projectx.ui.common.pickers.DataPickerScreen
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun InterestSelectionScreen(
    state: InterestSelectionState,
    onInit: () -> Unit,
    onInterestClick: (String) -> Unit,
    onSubmit: (NavController) -> Unit,
    onCancel: (NavController) -> Unit
) {
    LaunchedEffect(Unit) {
        onInit()
    }

    val navController = LocalNavController.current

    DataPickerScreen(
        title = "Select interest",
        onBackClicked = { onCancel(navController) },
        onSubmit = { onSubmit(navController) }
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
