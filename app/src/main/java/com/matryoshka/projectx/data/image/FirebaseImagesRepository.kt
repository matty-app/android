package com.matryoshka.projectx.data.repository.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.matryoshka.projectx.data.image.ImagesRepository
import com.matryoshka.projectx.exception.ProjectxException
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

private const val TAG = "FirebaseImagesRepo"
private const val IMAGES_PATH = "images/"
private const val AVATARS_PATH = "${IMAGES_PATH}avatars/"

class FirebaseImagesRepository @Inject constructor(storage: FirebaseStorage) :
    ImagesRepository {
    private val reference = storage.reference

    override suspend fun saveAvatar(userUid: String, sourceUri: Uri) {
        saveImage(sourceUri, getAvatarStoragePath(userUid))
    }

    override suspend fun getAvatar(userUid: String, file: File): File? {
        return getImage(getAvatarStoragePath(userUid), file)
    }

    override suspend fun deleteAvatar(userUid: String) {
        deleteImage(getAvatarStoragePath(userUid))
    }

    private suspend fun saveImage(uri: Uri, storagePath: String) {
        try {
            reference.child(storagePath).putFile(uri).await()
        } catch (ex: Exception) {
            Log.d(TAG, "saveImage: ${ex.message}")
            throw ImagesRepositoryException(ex)
        }
    }

    private suspend fun getImage(storagePath: String, file: File): File? {
        return try {
            reference.child(storagePath).getFile(file).await()
            file
        } catch (ex: StorageException) {
            if (ex.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) return null
            throw ImagesRepositoryException(ex)
        } catch (ex: Exception) {
            Log.d(TAG, "saveImage: ${ex.message}")
            throw ImagesRepositoryException(ex)
        }
    }

    private suspend fun deleteImage(storagePath: String) {
        try {
            reference.child(storagePath).delete().await()
        } catch (ex: Exception) {
            Log.d(TAG, "saveImage: ${ex.message}")
            throw ImagesRepositoryException(ex)
        }
    }

    private fun getAvatarStoragePath(userUid: String) = "$AVATARS_PATH$userUid.jpg"
}

class ImagesRepositoryException(cause: Throwable?) : ProjectxException(cause)