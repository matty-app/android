package com.matryoshka.projectx.ui.event.editing

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.ui.common.ListItem
import com.matryoshka.projectx.ui.common.ScreenStatus.LOADING
import com.matryoshka.projectx.ui.common.ScreenStatus.READY
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InterestSelectionScreen(
    state: InterestSelectionState,
    onInterestClick: (Interest) -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = "Select interest", onBackClicked = onCancel)
        }
    ) {
        if (state.status == LOADING) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        if (state.status == READY) {
            LazyColumn {
                items(state.interests) { interest ->
                    InterestRow(
                        interest = interest,
                        onClick = onInterestClick,
                        checked = interest.id == state.selectedInterestId
                    )
                }
            }
        }
    }
}

@Composable
private fun InterestRow(interest: Interest, onClick: (Interest) -> Unit, checked: Boolean) {
    ListItem(
        modifier = Modifier.clickable { onClick(interest) },
        text = {
            Row {
                Text(text = interest.name)
                interest.emoji?.let { emoji ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = emoji)
                }
            }
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
                status = READY,
                interests = listOf(
                    Interest(id = "football", name = "football"),
                    Interest(id = "piano", name = "piano"),
                    Interest(id = "books", name = "books"),
                ),
                selectedInterestId = "piano"
            ),
            onInterestClick = {},
            onCancel = {}
        )
    }
}
