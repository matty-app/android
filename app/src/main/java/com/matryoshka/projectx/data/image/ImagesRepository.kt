package com.matryoshka.projectx.data.image

import android.net.Uri
import java.io.File

interface ImagesRepository {
    suspend fun saveAvatar(userUid: String, sourceUri: Uri)

    suspend fun getAvatar(userUid: String, file: File): File?

    suspend fun deleteAvatar(userUid: String)
}