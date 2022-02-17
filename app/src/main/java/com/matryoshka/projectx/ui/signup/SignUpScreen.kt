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
import androidx.compose.material.Button
import androidx.compose.material.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.fields.TextField
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun SignUpScreen(
    onRegisterClicked: () -> Unit,
    onSignInClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_outdoor_adventure),
            contentDescription = stringResource(id = R.string.outdoor_adventure),
            modifier = Modifier
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    style = MaterialTheme.typography.h4
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                placeholder = stringResource(id = R.string.name),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_account_circle),
                        contentDescription = ""
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                placeholder = stringResource(id = R.string.email),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = ""
                    )
                }
            )
            Spacer(modifier = Modifier.height(64.dp))

            Button(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onRegisterClicked() }
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
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.sign_in),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .clickable { onSignInClicked() }
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
            onRegisterClicked = {},
            onSignInClicked = {}
        )
    }
}