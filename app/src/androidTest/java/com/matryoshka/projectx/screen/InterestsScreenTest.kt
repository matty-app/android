package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import com.matryoshka.projectx.ui.interests.InterestsScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InterestsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setContent() {
        composeRule.setContent {
            InterestsScreen()
        }
    }

    @Test
    fun shouldInterestBeClickable() {
        composeRule.onAllNodesWithTag("interest")[0]
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun shouldNextButtonBeClickable() {
        composeRule.onNodeWithText("Next").assertIsDisplayed().assertHasClickAction()
    }
}