package com.matryoshka.projectx.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ErrorToast
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.TextField
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun SignUpScreen(
    state: SignUpScreenState,
    onRegisterClicked: () -> Unit,
    onSignInClicked: () -> Unit
) {
    val error = state.error
    val enabled = state.enabled

    if (state.isProgressIndicatorVisible) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    if (state.isErrorToastVisible) {
        ErrorToast(error = error!!)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_outdoor_adventure),
            contentDescription = stringResource(id = R.string.outdoor_adventure),
            modifier = Modifier.height(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                inputField = state.nameField,
                placeholder = stringResource(id = R.string.name),
                enabled = enabled,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_circle),
                        contentDescription = ""
                    )
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                inputField = state.emailField,
                placeholder = stringResource(id = R.string.email),
                enabled = enabled,
                keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = ""
                    )
                }
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                shape = RoundedCornerShape(12.dp),
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onRegisterClicked
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.already_member),
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.sign_in),
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .clickable { if (enabled) onSignInClicked() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    ProjectxTheme {
        SignUpScreen(
            state = SignUpScreenState(
                nameField = FieldState("Vasya"),
                emailField = FieldState("vasya@gmail.com")
            ),
            onRegisterClicked = {},
            onSignInClicked = {}
        )
    }
}