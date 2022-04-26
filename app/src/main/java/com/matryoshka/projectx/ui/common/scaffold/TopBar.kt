package com.matryoshka.projectx.ui.common.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.matryoshka.projectx.R
import com.matryoshka.projectx.navigation.navToUserProfileScreen

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        elevation = elevation,
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Normal
            )
        },
        navigationIcon = onBackClicked?.let {
            {
                Icon(
                    Icons.Outlined.ArrowBackIos,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        onBackClicked()
                    },
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        actions = actions
    )
}

@Composable
fun UserProfileButton(navController: NavController) {
    Icon(
        painter = painterResource(id = R.drawable.ic_account_circle),
        contentDescription = stringResource(R.string.user_profile),
        modifier = Modifier
            .size(32.dp)
            .clickable { navController.navToUserProfileScreen() },
        tint = MaterialTheme.colors.primary
    )
}