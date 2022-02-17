package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.matryoshka.projectx.ui.email.EmailConfirmationScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EmailConfirmationScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setContent() {
        composeRule.setContent {
            EmailConfirmationScreen(
                onBackClicked = {}
            )
        }
    }

    @Test
    fun shouldShowTapTheLink() {
        composeRule.onNodeWithText(
            text = "Tap the link",
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun shouldBackArrowBeClickable() {
        composeRule.onNodeWithTag("backArrow").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldSendAgainLinkBeClickable() {
        composeRule.onNodeWithText("Send again").assertIsDisplayed().assertHasClickAction()
    }
}