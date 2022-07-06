package com.matryoshka.projectx.data.image

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

const val IMAGES_DIRECTORY = "images"
const val AVATARS_DIRECTORY = "avatars"

@Singleton
class LocalImagesRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val imagesDir = File(context.filesDir, IMAGES_DIRECTORY)
    private val avatarsDir = File(imagesDir, AVATARS_DIRECTORY)

    init {
        if (!imagesDir.exists())
            imagesDir.mkdir()
        if (!avatarsDir.exists())
            avatarsDir.mkdir()
    }

    fun avatarExists(userId: String) = getAvatarFile(userId).exists()

    fun getAvatarUri(userId: String) = getImageUri(getAvatarFile(userId))

    fun saveImage(oldUri: Uri, newUri: Uri) {
        context.contentResolver.openInputStream(oldUri)?.use { input ->
            context.contentResolver.openOutputStream(newUri)?.use {
                input.copyTo(it)
            }
        }
    }

    fun deleteAvatar(userId: String): Boolean = getAvatarFile(userId).delete()

    fun getAvatarFile(userId: String) = File(avatarsDir, "$userId.jpg")

    private fun getImageUri(file: File) = FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        file
    )
}