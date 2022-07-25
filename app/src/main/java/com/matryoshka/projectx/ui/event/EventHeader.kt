package com.matryoshka.projectx.ui.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.event.Event
import com.matryoshka.projectx.ui.theme.LightGray
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun EventHeader(
    event: Event,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
}

@Preview(showBackground = true)
@Composable
private fun EventsHeaderPreview() {
    ProjectxTheme {
        EventHeader(event = eventPreview)
    }
}