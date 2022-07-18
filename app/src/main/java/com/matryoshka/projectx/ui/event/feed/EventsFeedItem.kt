package com.matryoshka.projectx.ui.event.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.ui.event.EventHeader
import com.matryoshka.projectx.ui.event.eventPreview
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import java.time.format.DateTimeFormatter

@Composable
fun EventsFeedItem(
    event: Event,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            EventHeader(event = event)
            Spacer(modifier = Modifier.height(8.dp))
            if (event.summary.isNotBlank()) {
                Text(
                    text = event.summary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = event.location.name ?: stringResource(id = R.string.no_location),
                    color = MaterialTheme.colors.primary,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )

                val formattedDate = remember(event.startDate) {
                    val dateTimeFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm")
                    event.startDate.format(dateTimeFormat)
                }

                Text(
                    text = formattedDate,
                    color = MaterialTheme.colors.primary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsFeedItemPreview() {
    ProjectxTheme {
        EventsFeedItem(
            event = eventPreview
        )
    }
}