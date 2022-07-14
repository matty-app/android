package com.matryoshka.projectx.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.ui.theme.Green
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import com.matryoshka.projectx.ui.theme.Red
import com.matryoshka.projectx.ui.theme.Yellow
import com.matryoshka.projectx.utils.hoursPart
import com.matryoshka.projectx.utils.minutesPart
import java.time.Duration
import java.time.LocalDateTime

private val VERY_SOON = Duration.ofHours(3)
private val SOON = Duration.ofDays(2)

@Composable
fun RemainingTimeBadge(dateTime: LocalDateTime) {
    val remainingTime: Duration = remember(dateTime) {
        Duration.between(LocalDateTime.now(), dateTime)
    }

    val (backgroundColor, textColor) = remember(remainingTime) {
        when {
            remainingTime < VERY_SOON -> {
                Red to Color.White
            }
            remainingTime < SOON -> {
                Yellow to Color.Black
            }
            else -> {
                Green to Color.Black
            }
        }
    }

    val remainingTimeString: String = remember(remainingTime) {
        val remainingDays = remainingTime.toDays()
        val remainingHours = remainingTime.hoursPart
        val remainingMinutes = remainingTime.minutesPart

        val remainingDaysString = if (remainingDays > 0) "${remainingDays}d" else ""
        val remainingHoursString = if (remainingHours > 0) "${remainingHours}h" else ""
        val remainingMinutesString =
            if (remainingMinutes > 0 && remainingDays == 0L) "${remainingMinutes}m" else ""
        "$remainingDaysString $remainingHoursString $remainingMinutesString"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(80.dp)
            .height(24.dp)
            .clip(CircleShape)
            .background(color = backgroundColor)
    ) {
        Text(
            text = remainingTimeString,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RemainingTimeBadgeGreen() {
    ProjectxTheme {
        RemainingTimeBadge(dateTime = LocalDateTime.now().plusDays(30))
    }
}

@Preview(showBackground = true)
@Composable
private fun RemainingTimeBadgeYellow() {
    ProjectxTheme {
        RemainingTimeBadge(dateTime = LocalDateTime.now().plusDays(1))
    }
}

@Preview(showBackground = true)
@Composable
private fun RemainingTimeBadgeRed() {
    ProjectxTheme {
        RemainingTimeBadge(dateTime = LocalDateTime.now().plusHours(1))
    }
}