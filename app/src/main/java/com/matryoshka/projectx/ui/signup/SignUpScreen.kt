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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.fields.TextField
import com.matryoshka.projectx.ui.theme.DarkGray
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun SignUpScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 11.dp, vertical = 28.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_outdoor_adventure),
            contentDescription = stringResource(id = R.string.outdoor_adventure),
            modifier = Modifier
                .width(330.dp)
                .height(230.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(
                    align = Alignment.BottomCenter
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sign up",
                    style = MaterialTheme.typography.h4
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextField(
                placeholder = stringResource(id = R.string.name),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_account_circle),
                        contentDescription = ""
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                placeholder = stringResource(id = R.string.email),
                trailingIcon = {
                    Image(
                        imageVector = Icons.Default.Email,
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(color = DarkGray)
                    )
                }
            )
            Spacer(modifier = Modifier.height(64.dp))

            Button(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = { /*TODO*/ }
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
                        .clickable { /*TODO*/ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    ProjectxTheme {
        SignUpScreen()
    }
}