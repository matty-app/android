package com.matryoshka.projectx.ui.userprofile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.ui.common.FieldState
import com.matryoshka.projectx.ui.common.FlexRow
import com.matryoshka.projectx.ui.common.ScreenStatus
import com.matryoshka.projectx.ui.common.TextField
import com.matryoshka.projectx.ui.common.scaffold.NavigationBottomBar
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.theme.ProjectxTheme
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserProfileScreen(
    navController: NavController,
    state: UserProfileScreenState,
    actions: UserProfileActions
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.03f),
        topBar = {
            TopBar(
                title = stringResource(id = R.string.user_profile),
                onBackClicked = { actions.onBackClick(navController) }
            )
        },
        bottomBar = {
            NavigationBottomBar(navController = navController)
        }
    ) {
        if (state.isProgressIndicatorVisible) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (state.status == ScreenStatus.READY) {
            UserProfileBody(
                navController = navController,
                state = state,
                actions = actions
            )
        }
    }
}

@Composable
private fun UserProfileBody(
    navController: NavController,
    state: UserProfileScreenState,
    actions: UserProfileActions
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 28.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        ChangeValueDialog(
            display = state.displayNameDialog,
            state = state.formState.nameField,
            title = stringResource(id = R.string.name),
            onClose = { actions.setDisplayNameDialog(false) },
            onSave = actions.onNameChanged
        )

        ChangeValueDialog(
            display = state.displayEmailDialog,
            state = state.formState.emailField,
            title = stringResource(id = R.string.email),
            keyboardType = KeyboardType.Email,
            warning = stringResource(id = R.string.email_link_will_be_sent),
            onClose = { actions.setDisplayEmailDialog(false) },
            onSave = { actions.onEmailChanged(navController) }
        )

        ChangeValueDialog(
            display = state.displayAboutMeDialog,
            state = state.formState.aboutMeField,
            title = stringResource(id = R.string.about_me),
            imeAction = ImeAction.Default,
            maxLines = 5,
            onClose = { actions.setDisplayAboutMeDialog(false) },
            onSave = actions.onAboutMeChanged
        )

        Column(
            verticalArrangement = Arrangement.spacedBy((-32).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(150.dp)
        ) {
            Photo(
                photoUri = state.photoUri,
                photoExists = state.photoExists,
                displayPhotoDialog = state.displayPhotoDialog,
                setDisplayPhotoDialog = actions.setDisplayPhotoDialog,
                onRemovePhotoClicked = actions.onRemovePhotoClick,
                takePhoto = actions.takePhoto,
                selectPhoto = actions.selectPhoto,
            )
        }
        UserProfileItem(
            labelText = stringResource(id = R.string.name),
            onClick = { actions.setDisplayNameDialog(true) }
        ) {
            Text(text = state.name)
        }
        UserProfileItem(
            labelText = stringResource(id = R.string.email),
            onClick = { actions.setDisplayEmailDialog(true) }
        ) {
            Text(text = state.email)
        }
        UserProfileItem(
            labelText = stringResource(id = R.string.about_me),
            onClick = { actions.setDisplayAboutMeDialog(true) }
        ) {
            Text(text = state.aboutMe)
        }
        UserProfileItem(
            labelText = stringResource(id = R.string.interests),
            onClick = { actions.onInterestsClick(navController) }
        ) {
            FlexRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalGap = 12.dp,
                horizontalGap = 12.dp,
                alignment = Alignment.CenterHorizontally
            ) {
                state.interests.forEach {
                    Interest(name = it.name)
                }
            }
        }
    }
}

@Composable
private fun UserProfileItem(
    labelText: String,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(
                modifier = Modifier
                    .width(12.dp)
                    .weight(weight = 1f, fill = true)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ChangeValueDialog(
    display: Boolean,
    state: FieldState<String>,
    title: String,
    warning: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    maxLines: Int = 1,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    if (display) {
        AlertDialog(
            onDismissRequest = {
                onClose()
            },
            buttons = {
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(Unit) {
                    //delay before request focus (awaitFrame() doesn't work every time)
                    //https://issuetracker.google.com/issues/204502668
                    delay(200)
                    focusRequester.requestFocus()
                }

                Column(modifier = Modifier.padding(12.dp)) {
                    TextField(
                        fieldState = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        maxLines = maxLines,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = imeAction
                        ),
                        keyboardActions = KeyboardActions(onDone = { onSave() })
                    )
                    warning?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = it)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onClose
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = onSave
                        ) {
                            Text(
                                text = stringResource(R.string.save),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .wrapContentHeight(),
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            },
            backgroundColor = MaterialTheme.colors.background,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        )
    }
}

@Composable
private fun Interest(name: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(30.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f)
            )
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    ProjectxTheme {
        UserProfileScreen(
            navController = rememberNavController(),
            state = UserProfileScreenState(
                name = "John",
                email = "john@gmail.com",
                aboutMe = "I like to swim",
                interests = interestsPreview
            ),
            actions = UserProfileActions()
        )
    }
}

val interestsPreview = listOf(
    Interest("Metallica", "Metallica"),
    Interest("Coding", "Coding"),
    Interest("World of Warcraft", "World of Warcraft"),
    Interest("Dancing", "Dancing"),
    Interest("Guitar", "Guitar")
)