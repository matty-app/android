package com.matryoshka.projectx.ui.email

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.ErrorToast
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.XShaker
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.theme.DarkGray
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EmailConfirmationScreen(
    state: EmailConfirmationScreenState,
    email: String,
    onBackClicked: () -> Unit,
    onSendAgainClicked: () -> Unit,
    onCodeChanged: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.check_email),
                onBackClicked = onBackClicked
            )
        }
    ) {
        val status = state.status
        val error = state.error
        if (status == ScreenStatus.ERROR && error != null) {
            ErrorToast(error = error)
        }
        if (state.showProgress) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 28.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mailbox),
                contentDescription = "",
                modifier = Modifier.height(160.dp)
            )
            Text(
                text = stringResource(id = R.string.enter_code),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = email,
                color = DarkGray,
                modifier = Modifier.fillMaxWidth()
            )
            XShaker(state.shakerState) {
                VerificationCode(
                    state = state.verificationCodeState,
                    onChanged = onCodeChanged
                )
            }
            Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
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
            onSendAgainClicked = {},
            onCodeChanged = {}
        )
    }
}