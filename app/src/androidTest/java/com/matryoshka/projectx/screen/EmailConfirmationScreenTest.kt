package com.matryoshka.projectx.screen

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

class EmailConfirmationScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

//    private fun setContent(
//        onBackClicked: () -> Unit = {},
//        onSendAgainClicked: () -> Unit = {},
//    ) {
//        composeRule.setContent {
//            EmailConfirmationScreen(
//                state = EmailConfirmationScreenState(),
//                email = "john@gmail.com",
//                onBackClicked = onBackClicked,
//                onSendAgainClicked = onSendAgainClicked
//            )
//        }
//    }
//
//    @Test
//    fun shouldShowTapTheLink() {
//        setContent()
//
//        composeRule.onNodeWithText(
//            text = "Tap the link",
//            substring = true
//        ).assertIsDisplayed()
//    }
//
//    @Test
//    fun shouldCallBackAction() {
//        val backAction = TestAction("Back action")
//        setContent(onBackClicked = backAction::call)
//
//        composeRule.onNodeWithTag("backArrow")
//            .assertIsDisplayed()
//            .assertHasClickAction()
//            .performClick()
//
//        backAction.assertIsCalled()
//    }
//
//    @Test
//    fun shouldCallSendAgainAction() {
//        val sendAgainAction = TestAction("Send again action")
//        setContent(onSendAgainClicked = { sendAgainAction.call() })
//
//        composeRule.onNodeWithText("Send again")
//            .assertIsDisplayed()
//            .assertHasClickAction()
//            .performClick()
//
//        sendAgainAction.assertIsCalled()
//    }
}