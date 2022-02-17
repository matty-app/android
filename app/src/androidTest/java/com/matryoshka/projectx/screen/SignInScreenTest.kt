package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.matryoshka.projectx.ui.signin.SignInScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setContent() {
        composeRule.setContent {
            SignInScreen(
                onLogInClicked = {},
                onSignUpClicked = {}
            )
        }
    }

    @Test
    fun shouldShowEmailField() {
        composeRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun shouldLogInButtonBeClickable() {
        composeRule.onNodeWithText("Log in").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldSignUpLinkBeClickable() {
        composeRule.onNodeWithText("Sign up").assertIsDisplayed().assertHasClickAction()
    }
}