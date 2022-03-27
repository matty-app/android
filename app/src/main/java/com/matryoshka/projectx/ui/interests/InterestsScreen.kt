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
import androidx.compose.material.LinearProgressIndicator
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
import com.matryoshka.projectx.data.Interest
import com.matryoshka.projectx.ui.common.ErrorToast
import com.matryoshka.projectx.ui.common.FlexRow
import com.matryoshka.projectx.ui.theme.Gray
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import com.matryoshka.projectx.data.Interest as InterestModel

private const val FOOTER_HEIGHT = 130

@Composable
fun InterestsScreen(state: InterestsScreenState, onNextClicked: () -> Unit) {

    if (state.isProgressIndicatorVisible) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    if (state.isErrorToastVisible) {
        ErrorToast(error = state.error!!)
    }

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
                Interests(state.interests)
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
                    onClick = onNextClicked
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
private fun Interests(interests: Collection<InterestState>) {
    FlexRow(
        verticalGap = 12.dp,
        horizontalGap = 12.dp,
        extraBottomSpace = FOOTER_HEIGHT.dp,
        alignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(state = ScrollState(0))
    ) {
        interests.forEach {
            Interest(interestState = it)
        }
    }
}

@Composable
private fun Interest(interestState: InterestState) {
    if (!interestState.isSelected) {
        UnselectedInterest(
            interest = interestState.interest,
            onSelect = interestState::onChangeSelection
        )
    } else {
        SelectedInterest(
            interest = interestState.interest,
            onDeselect = interestState::onChangeSelection
        )
    }
}

@Composable
fun UnselectedInterest(interest: Interest, onSelect: () -> Unit) {
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
                indication = null,
                onClick = onSelect
            )
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
fun SelectedInterest(interest: Interest, onDeselect: () -> Unit) {
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
                indication = null,
                onClick = onDeselect
            )
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
        InterestsScreen(
            state = InterestsScreenState(interests = interestsPreview),
            onNextClicked = {}
        )
    }
}

val interestsPreview = listOf(
    InterestState(InterestModel("coding", "Coding")),
    InterestState(InterestModel("sport", "Sport")),
    InterestState(InterestModel("wow", "World of Warcraft")).apply { onChangeSelection() },
    InterestState(InterestModel("football", "Football")),
    InterestState(InterestModel("singing", "Singing")),
    InterestState(InterestModel("rock_music", "Rock music")).apply { onChangeSelection() },
    InterestState(InterestModel("hip_hop", "Hip-hop")).apply { onChangeSelection() },
    InterestState(InterestModel("guitar", "Guitar")),
    InterestState(InterestModel("piano", "Piano")),
    InterestState(InterestModel("lego", "LEGO")),
    InterestState(InterestModel("cars", "Cars")),
    InterestState(InterestModel("cycling", "Cycling")),
    InterestState(InterestModel("metallica", "Metallica")),
    InterestState(InterestModel("michael_jackson", "Michael Jackson")),
    InterestState(InterestModel("lotr", "The Lord of the Rings")),
    InterestState(
        InterestModel(
            "lotr_p1",
            "The Lord of the Rings: The Battle for Middle-earth Part First"
        )
    ),
    InterestState(
        InterestModel(
            "lotr_p2",
            "The Lord of the Rings: The Battle for Middle-earth Part Second"
        )
    ).apply { onChangeSelection() },
    InterestState(InterestModel("cs_go", "CS:GO")),
    InterestState(InterestModel("basketball", "Basketball")),
    InterestState(InterestModel("documentary", "Documentary")).apply { onChangeSelection() },
    InterestState(InterestModel("dancing", "Dancing")),
    InterestState(
        InterestModel(
            "narnia",
            "The Chronicles of Narnia: The Lion, the Witch and the Wardrobe"
        )
    ).apply { onChangeSelection() },
    InterestState(
        InterestModel(
            "russian_folk_songs",
            "Russian folk songs"
        )
    ).apply { onChangeSelection() },
    InterestState(InterestModel("psychology", "Psychology")).apply { onChangeSelection() },
    InterestState(InterestModel("hp1", "Harry Potter and the Philosopher's Stone")),
    InterestState(InterestModel("hp2", "Harry Potter and the Chamber of Secrets")),
    InterestState(InterestModel("fishing", "Fishing")).apply { onChangeSelection() },
    InterestState(InterestModel("debating_club", "Debating club")),
    InterestState(InterestModel("hp3", "Harry Potter and the Prisoner of Azkaban")),
    InterestState(
        InterestModel(
            "hp4",
            "Harry Potter and the Goblet of Fire"
        )
    ).apply { onChangeSelection() },
    InterestState(InterestModel("boxing", "Boxing")),
    InterestState(InterestModel("bowling", "Bowling")),
    InterestState(InterestModel("hp5", "Harry Potter and the Order of the Phoenix")),
    InterestState(InterestModel("hp6", "Harry Potter and the Half-Blood Prince")),
    InterestState(InterestModel("hp7", "Harry Potter and the Deathly Hallows")),
    InterestState(InterestModel("politics", "Politics")),
    InterestState(InterestModel("physics", "Physics")),
)