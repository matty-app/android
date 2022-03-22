package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.matryoshka.projectx.support.TestAction
import com.matryoshka.projectx.ui.common.InputField
import com.matryoshka.projectx.ui.signup.SignUpScreen
import com.matryoshka.projectx.ui.signup.SignUpScreenState
import org.junit.Rule
import org.junit.Test

class SignUpScreenTest {
    @get:Rule
    val composeRule = createComposeRule()
    val name = "John"
    val email = "john@gmail.com"

    private fun setContent(
        onRegisterClicked: () -> Unit = {},
        onSignInClicked: () -> Unit = {},
    ) {
        composeRule.setContent {
            SignUpScreen(
                state = SignUpScreenState(
                    nameField = InputField(name),
                    emailField = InputField(email)
                ),
                onRegisterClicked = onRegisterClicked,
                onSignInClicked = onSignInClicked
            )
        }
    }

    @Test
    fun shouldShowNameField() {
        setContent()

        composeRule.onNodeWithText(name).assertIsDisplayed()
    }

    @Test
    fun shouldShowEmailField() {
        setContent()

        composeRule.onNodeWithText(email).assertIsDisplayed()
    }

    @Test
    fun shouldRegisterButtonBeClickable() {
        setContent()

        composeRule.onNodeWithText("Register").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldSignInLinkBeClickable() {
        setContent()

        composeRule.onNodeWithText("Sign in").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldCallRegisterAction() {
        val registerAction = TestAction("Register action")
        setContent(onRegisterClicked = registerAction::call)

        composeRule.onNodeWithText("Register").performClick()

        registerAction.assertIsCalled()
    }

    @Test
    fun shouldCallSigInAction() {
        val signInAction = TestAction("Sign In action")
        setContent(onSignInClicked = signInAction::call)

        composeRule.onNodeWithText("Sign in").performClick()

        signInAction.assertIsCalled()
    }
}