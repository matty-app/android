package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.matryoshka.projectx.support.TestAction
import com.matryoshka.projectx.ui.common.InputField
import com.matryoshka.projectx.ui.signin.SignInScreen
import com.matryoshka.projectx.ui.signin.SignInScreenState
import org.junit.Rule
import org.junit.Test

class SignInScreenTest {
    @get:Rule
    val composeRule = createComposeRule()
    val email = "john@gmail.com"

    private fun setContent(
        onLogInClicked: () -> Unit = {},
        onSignUpClicked: () -> Unit = {},
    ) {
        composeRule.setContent {
            SignInScreen(
                state = SignInScreenState(emailField = InputField(email)),
                onLogInClicked = onLogInClicked,
                onSignUpClicked = onSignUpClicked
            )
        }
    }

    @Test
    fun shouldShowEmailField() {
        setContent()

        composeRule.onNodeWithText(email).assertIsDisplayed()
    }

    @Test
    fun shouldLogInButtonBeClickable() {
        setContent()

        composeRule.onNodeWithText("Log in").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldSignUpLinkBeClickable() {
        setContent()

        composeRule.onNodeWithText("Sign up").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun shouldCallLogInAction() {
        val logInAction = TestAction("Log In action")
        setContent(onLogInClicked = logInAction::call)

        composeRule.onNodeWithText("Log in").performClick()

        logInAction.assertIsCalled()
    }

    @Test
    fun shouldCallSignUpAction() {
        val signUpAction = TestAction("Sign Up action")
        setContent(onSignUpClicked = signUpAction::call)

        composeRule.onNodeWithText("Sign up").performClick()

        signUpAction.assertIsCalled()
    }
}