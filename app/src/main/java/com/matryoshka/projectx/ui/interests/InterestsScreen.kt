package com.matryoshka.projectx.ui.interests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.FlexRow
import com.matryoshka.projectx.ui.theme.Gray
import com.matryoshka.projectx.ui.theme.ProjectxTheme

private const val FOOTER_HEIGHT = 130

@Composable
fun InterestsScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 28.dp)
    ) {
        Text(
            text = stringResource(id = R.string.what_interests),
            style = MaterialTheme.typography.h5,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.choose_interests),
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(-FOOTER_HEIGHT.dp)) {
            Box(modifier = Modifier.fillMaxSize()) {
                Interests()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.height(FOOTER_HEIGHT.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                )
                            )
                        )
                )
                Button(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun Interests() {
    FlexRow(
        verticalGap = 12.dp,
        horizontalGap = 12.dp,
        extraBottomSpace = FOOTER_HEIGHT.dp,
        alignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(state = ScrollState(0))
    ) {
        for (interest in interests) {
            if (!interest.isSelected) {
                Interest(interest = interest)
            } else {
                SelectedInterest(interest = interest)
            }
        }
    }
}

@Composable
private fun Interest(interest: Interest) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(30.dp)
            .clip(CircleShape)
            .border(
                border = BorderStroke(width = 1.dp, color = Gray),
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /*TODO*/ }
            .testTag("interest")
    ) {
        Text(
            text = interest.name,
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Composable
private fun SelectedInterest(interest: Interest) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(30.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colors.secondary
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /*TODO*/ }
            .testTag("interest")
    ) {
        Text(
            text = interest.name,
            style = MaterialTheme.typography.subtitle1,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterestsScreenPreview() {
    ProjectxTheme {
        InterestsScreen()
    }
}

val interests = listOf(
    Interest("Coding"),
    Interest("Sport"),
    Interest("World of Warcraft", true),
    Interest("Football"),
    Interest("Singing"),
    Interest("Rock music", true),
    Interest("Hip-hop", true),
    Interest("Guitar"),
    Interest("Piano"),
    Interest("LEGO"),
    Interest("Cars"),
    Interest("Cycling"),
    Interest("Metallica"),
    Interest("Michael Jackson"),
    Interest("The Lord of the Rings"),
    Interest("The Lord of the Rings: The Battle for Middle-earth Part First"),
    Interest("The Lord of the Rings: The Battle for Middle-earth Part Second", true),
    Interest("CS:GO"),
    Interest("Basketball"),
    Interest("Documentary", true),
    Interest("Dancing"),
    Interest("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe", true),
    Interest("Russian folk songs", true),
    Interest("Psychology", true),
    Interest("Harry Potter and the Philosopher's Stone"),
    Interest("Harry Potter and the Chamber of Secrets"),
    Interest("Fishing", true),
    Interest("Debating club"),
    Interest("Harry Potter and the Prisoner of Azkaban"),
    Interest("Harry Potter and the Goblet of Fire", true),
    Interest("Boxing"),
    Interest("Bowling"),
    Interest("Harry Potter and the Order of the Phoenix"),
    Interest("Harry Potter and the Half-Blood Prince"),
    Interest("Harry Potter and the Deathly Hallows"),
    Interest("Politics"),
    Interest("Physics"),
)

data class Interest(val name: String, val isSelected: Boolean = false)