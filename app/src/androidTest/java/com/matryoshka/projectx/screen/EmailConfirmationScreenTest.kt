package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.matryoshka.projectx.support.TestAction
import org.junit.Rule
import org.junit.Test

class EmailConfirmationScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun setContent(
        onBackClicked: () -> Unit = {},
        onSendAgainClicked: () -> Unit = {},
    ) {
        val handler = { email: String -> print(email) }
//        composeRule.setContent {
//            EmailConfirmationScreen(
//                onBackClicked = onBackClicked,
//                onSendAgainClicked = handler
//            )
//        }
    }

    @Test
    fun shouldShowTapTheLink() {
        setContent()

        composeRule.onNodeWithText(
            text = "Tap the link",
            substring = true
        ).assertIsDisplayed()
    }

    @Test
    fun shouldBackArrowBeClickable() {
        setContent()

        composeRule.onNodeWithTag("backArrow").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldSendAgainLinkBeClickable() {
        setContent()

        composeRule.onNodeWithText("Send again").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldCallBackAction() {
        val backAction = TestAction("Back action")
        setContent(onBackClicked = backAction::call)

        composeRule.onNodeWithTag("backArrow").performClick()

        backAction.assertIsCalled()
    }

    @Test
    fun shouldCallSendAgainAction() {
        val sendAgainAction = TestAction("Send again action")
        setContent(onSendAgainClicked = sendAgainAction::call)

        composeRule.onNodeWithText("Send again").performClick()

        sendAgainAction.assertIsCalled()
    }
}