package com.matryoshka.projectx.ui.userprofile

import android.content.Context
import android.net.Uri
import androidx.navigation.NavController

data class UserProfileActions(
    val onBackClick: (NavController) -> Unit = {},
    val onRemovePhotoClick: () -> Unit = {},
    val takePhoto: (context: Context) -> Unit = {},
    val selectPhoto: (context: Context, uri: Uri) -> Unit = { _, _ -> },
    val onInterestsClick: (navController: NavController) -> Unit = {},
    val onNameChanged: () -> Unit = {},
    val onEmailChanged: (navController: NavController) -> Unit = {},
    val onAboutMeChanged: () -> Unit = {},
    val setDisplayPhotoDialog: (isDisplayed: Boolean) -> Unit = {},
    val setDisplayNameDialog: (isDisplayed: Boolean) -> Unit = {},
    val setDisplayEmailDialog: (isDisplayed: Boolean) -> Unit = {},
    val setDisplayAboutMeDialog: (isDisplayed: Boolean) -> Unit = {}
)