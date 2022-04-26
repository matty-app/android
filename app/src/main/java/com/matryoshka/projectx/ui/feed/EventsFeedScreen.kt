package com.matryoshka.projectx.ui.feed

import android.annotation.SuppressLint
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.ui.common.scaffold.TopBar
import com.matryoshka.projectx.ui.common.scaffold.UserProfileButton

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EventsFeedScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = R.string.events_feed)) {
                UserProfileButton(navController)
            }
        }
    ) {
        Button(onClick = { navController.navigate(Screen.NEW_EVENT_SCREEN) }) {
            Text(text = "Create event", color = MaterialTheme.colors.onPrimary)
        }
    }
}