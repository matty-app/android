package com.matryoshka.projectx.ui.common.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.navigation.Screen
import com.matryoshka.projectx.navigation.currentScreenIs
import com.matryoshka.projectx.navigation.navToEventsFeedScreen
import com.matryoshka.projectx.navigation.navToUserProfileScreen
import com.matryoshka.projectx.ui.theme.ProjectxTheme

@Composable
fun NavigationBottomBar(
    navController: NavController
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationIcon(
                painter = rememberVectorPainter(image = Icons.Outlined.Search),
                text = stringResource(id = R.string.search),
                enable = false
            ) {
                //TODO(go to search screen)
            }
            NavigationIcon(
                painter = rememberVectorPainter(image = Icons.Filled.Menu),
                text = stringResource(id = R.string.events),
                enable = !navController.currentScreenIs(Screen.EVENTS_FEED_SCREEN)
            ) {
                navController.navToEventsFeedScreen()
            }
            NavigationIcon(
                painter = painterResource(id = R.drawable.ic_account_circle),
                text = stringResource(id = R.string.user_profile),
                enable = !navController.currentScreenIs(Screen.USER_PROFILE)
            ) {
                navController.navToUserProfileScreen()
            }
        }
    }
}

@Composable
private fun NavigationIcon(
    painter: Painter,
    text: String,
    enable: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { if (enable) onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painter,
            contentDescription = "", //TODO(add content description)
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colors.primary
        )
        Text(
            text = text,
            color = MaterialTheme.colors.primary,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsFeedScreenPreview() {
    ProjectxTheme {
        NavigationBottomBar(navController = rememberNavController())
    }
}