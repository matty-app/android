package com.matryoshka.projectx.ui.email

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ErrorToast
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.theme.DarkGray
import com.matryoshka.projectx.ui.theme.Gray
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun EmailConfirmationScreen(
    state: EmailConfirmationScreenState,
    email: String,
    onBackClicked: () -> Unit,
    onSendAgainClicked: () -> Unit
) {
    val status = state.status
    val error = state.error
    if (status == ScreenStatus.ERROR && error != null) {
        ErrorToast(error = error)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Gray,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("backArrow")
                    .clickable { onBackClicked() }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_mailbox),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = R.string.check_email),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.tap_email_link),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = email,
            color = DarkGray,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = true)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.didnt_get_email),
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.send_again),
                    style = MaterialTheme.typography.subtitle2,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .clickable { onSendAgainClicked() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailConfirmationScreenPreview() {
    ProjectxTheme {
        EmailConfirmationScreen(
            state = EmailConfirmationScreenState(),
            email = "azaza@email.ru",
            onBackClicked = {},
            onSendAgainClicked = {}
        )
    }
}