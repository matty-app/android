package com.matryoshka.projectx.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.matryoshka.projectx.support.TestAction
import com.matryoshka.projectx.ui.common.textFieldState
import com.matryoshka.projectx.ui.signin.SignInScreen
import com.matryoshka.projectx.ui.signin.SignInScreenState
import org.junit.Rule
import org.junit.Test

class SignInScreenTest {
    @get:Rule
    val composeRule = createComposeRule()
    private val email = "john@gmail.com"

    private fun setContent(
        onLogInClicked: () -> Unit = {},
        onSignUpClicked: () -> Unit = {},
    ) {
        composeRule.setContent {
            SignInScreen(
                onLogInClicked = onLogInClicked,
                onSignUpClicked = onSignUpClicked,
                state = SignInScreenState(emailField = textFieldState(email))
            )
        }
    }

    @Test
    fun shouldShowEmailField() {
        setContent()

        composeRule.onNodeWithText(email).assertIsDisplayed()
    }

    @Test
    fun shouldCallLogInAction() {
        val logInAction = TestAction("Log In action")
        setContent(onLogInClicked = logInAction::call)

        composeRule.onNodeWithText("Log in")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        logInAction.assertIsCalled()
    }

    @Test
    fun shouldCallSignUpAction() {
        val signUpAction = TestAction("Sign Up action")
        setContent(onSignUpClicked = signUpAction::call)

        composeRule.onNodeWithText("Sign up")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        signUpAction.assertIsCalled()
    }
}