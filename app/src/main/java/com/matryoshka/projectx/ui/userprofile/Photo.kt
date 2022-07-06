package com.matryoshka.projectx.ui.userprofile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.theme.LightGray

@Composable
fun Photo(
    photoUri: Uri?,
    photoExists: Boolean,
    displayPhotoDialog: Boolean,
    setDisplayPhotoDialog: (Boolean) -> Unit,
    onRemovePhotoClicked: () -> Unit,
    takePhoto: (Context) -> Unit,
    selectPhoto: (Context, Uri) -> Unit
) {
    val painter = when {
        photoExists -> rememberImagePainter(photoUri)
        else -> painterResource(id = R.drawable.no_avatar)
    }

    if (photoUri != null) {
        ChangePhotoDialog(
            display = displayPhotoDialog,
            photoUri = photoUri,
            photoExists = photoExists,
            close = { setDisplayPhotoDialog(false) },
            onRemovePhotoClicked = onRemovePhotoClicked,
            takePhoto = takePhoto,
            selectPhoto = selectPhoto
        )
    }

    Image(
        painter = painter,
        contentDescription = stringResource(id = R.string.user_avatar),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(LightGray)
    )
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(6.dp)
                    .size(32.dp)
                    .clickable { setDisplayPhotoDialog(true) }
            )
        }
    }
}

@Composable
private fun ChangePhotoDialog(
    display: Boolean,
    photoUri: Uri,
    photoExists: Boolean,
    close: () -> Unit,
    onRemovePhotoClicked: () -> Unit,
    takePhoto: (Context) -> Unit,
    selectPhoto: (Context, Uri) -> Unit
) {
    val context = LocalContext.current

    //TODO("request permissions for earlier Android versions")
    if (display) {
        val takePhotoLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isPhotoTaken ->
            if (isPhotoTaken) {
                takePhoto(context)
            }
        }

        val selectPhotoLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { selectedUri ->
            if (selectedUri != null) {
                selectPhoto(context, selectedUri)
            }
        }

        AlertDialog(
            onDismissRequest = {
                close()
            },
            title = {
                Text(
                    text = stringResource(R.string.change_photo),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    if (photoExists) {
                        Text(
                            text = stringResource(R.string.remove_photo),
                            modifier = Modifier.clickable {
                                onRemovePhotoClicked()
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Text(
                        text = stringResource(R.string.take_photo),
                        modifier = Modifier.clickable(onClick = {
                            takePhotoLauncher.launch(photoUri)
                        })
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.select_photo),
                        modifier = Modifier.clickable(onClick = {
                            selectPhotoLauncher.launch("image/*")
                        })
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = close
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            },
            modifier = Modifier.width(400.dp)
        )
    }
}