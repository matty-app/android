package com.matryoshka.projectx.ui.feed

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.matryoshka.projectx.navigation.Screen

@Composable
fun EventsFeedScreen(navController: NavController) {
    Button(onClick = { navController.navigate(Screen.NEW_EVENT_SCREEN) }) {
        Text(text = "Create event", color = MaterialTheme.colors.onPrimary)
    }
}