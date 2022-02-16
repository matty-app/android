package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.matryoshka.projectx.ui.signup.SignUpScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setContent() {
        composeRule.setContent {
            SignUpScreen(
                onSignInClicked = {}
            )
        }
    }

    @Test
    fun shouldShowNameField() {
        composeRule.onNodeWithText("Name").assertIsDisplayed()
    }

    @Test
    fun shouldShowEmailField() {
        composeRule.onNodeWithText("Email").assertIsDisplayed()
    }

    @Test
    fun shouldRegisterButtonBeClickable() {
        composeRule.onNodeWithText("Register").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldSignInLinkBeClickable() {
        composeRule.onNodeWithText("Sign in").assertIsDisplayed().assertHasClickAction()
    }
}