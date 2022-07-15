package com.matryoshka.projectx.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.ui.theme.LightGray
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import java.time.format.DateTimeFormatter

@Composable
fun EventsFeedItem(event: Event) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.no_avatar),
                    contentDescription = stringResource(id = R.string.user_avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(LightGray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(weight = 1f, fill = true)) {
                    Text(
                        text = event.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        event.interest.emoji?.let {
                            Text(text = it)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(text = event.interest.name)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                RemainingTimeBadge(dateTime = event.startDate)
            }
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
                    modifier = Modifier.clickable {
                        //TODO(go to map)
                    },
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