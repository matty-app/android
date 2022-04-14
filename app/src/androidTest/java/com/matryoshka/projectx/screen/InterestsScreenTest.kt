package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.matryoshka.projectx.data.Interest
import com.matryoshka.projectx.support.TestAction
import com.matryoshka.projectx.ui.interests.InterestState
import com.matryoshka.projectx.ui.interests.InterestsScreen
import com.matryoshka.projectx.ui.interests.InterestsScreenState
import com.matryoshka.projectx.ui.interests.SelectedInterest
import com.matryoshka.projectx.ui.interests.UnselectedInterest
import org.junit.Rule
import org.junit.Test

class InterestsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun setContent(
        interests: List<InterestState> = emptyList(),
        onNextClicked: () -> Unit = {}
    ) {
        composeRule.setContent {
            InterestsScreen(
                state = InterestsScreenState(interests),
                onNextClicked = onNextClicked
            )
        }
    }

    @Test
    fun shouldCallNextAction() {
        val nextAction = TestAction("Next action")
        setContent(onNextClicked = nextAction::call)

        composeRule.onNodeWithText("Next")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        nextAction.assertIsCalled()
    }

    @Test
    fun shouldSelectInterest() {
        val interestName = "Coding"
        val interest = Interest("coding", interestName)
        val selectAction = TestAction("Select interest action")
        composeRule.setContent {
            UnselectedInterest(interest = interest, onSelect = selectAction::call)
        }

        composeRule.onNodeWithText(interestName)
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        selectAction.assertIsCalled()
    }

    @Test
    fun shouldDeselectInterest() {
        val interestName = "Coding"
        val interest = Interest("coding", interestName)
        val selectAction = TestAction("Select interest action")
        composeRule.setContent {
            SelectedInterest(interest = interest, onDeselect = selectAction::call)
        }

        composeRule.onNodeWithText(interestName)
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        selectAction.assertIsCalled()
    }
}