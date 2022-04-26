package com.matryoshka.projectx.data.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.matryoshka.projectx.data.interest.FIRESTORE_INTERESTS
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.exception.SaveUserException
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

const val FIRESTORE_USERS = "users"
private const val COMPRESS_JPEG_QUALITY = 100
private const val DECODE_BYTE_ARRAY_OFFSET = 0

private const val TAG = "FirestoreUsersRepo"

class FirestoreUsersRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val interestsRepository: InterestsRepository
) : UsersRepository {

    override suspend fun save(user: User) {
        try {
            db.collection(FIRESTORE_USERS)
                .document(user.id)
                .set(user.toFirestore(db), SetOptions.merge())
                .await()
        } catch (ex: Exception) {
            Log.e(TAG, "saveUser ${user.id}: ${ex.message}")
            throw SaveUserException(ex)
        }
    }

    //@arg flat - skip loading related objects. setting default values instead
    override suspend fun getById(id: String, flat: Boolean): User? {
        val userFs = db.collection(FIRESTORE_USERS)
            .document(id)
            .get()
            .await()
            .toObject(FirestoreUser::class.java) ?: return null

        val interests = if (flat) emptyList() else {
            val ids = userFs.interests.map { it.id }
            interestsRepository.getByIds(ids)
        }

        return userFs.toDomain(interests)
    }

    override suspend fun getByIds(ids: List<String>): List<User> {
        return db.collection(FIRESTORE_USERS)
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .await()
            .toObjects(FirestoreUser::class.java)
            .map {
                User(
                    id = it.uid,
                    name = it.name!!,
                    email = it.email,
                )
            }

    }
}

private data class FirestoreUser(
    @DocumentId
    val uid: String = "",
    val name: String? = null,
    val email: String? = null,
    var aboutMe: String? = null,
    val thumbnail: Blob? = null,
    val interests: List<DocumentReference> = emptyList()
)

private fun FirestoreUser.toDomain(interests: List<Interest> = emptyList()) = User(
    id = uid,
    name = name!!,
    email = email,
    aboutMe = aboutMe,
    thumbnail = thumbnail?.let {
        val bytes = thumbnail.toBytes()
        BitmapFactory.decodeByteArray(bytes, DECODE_BYTE_ARRAY_OFFSET, bytes.size)
    },
    interests = interests,
)

private fun User.toFirestore(db: FirebaseFirestore) = FirestoreUser(
    uid = id,
    name = name,
    email = email,
    aboutMe = aboutMe,
    thumbnail = thumbnail?.let {
        ByteArrayOutputStream().use {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, COMPRESS_JPEG_QUALITY, it)
            Blob.fromBytes(it.toByteArray())
        }
    },
    interests = interests.map { db.collection(FIRESTORE_INTERESTS).document(it.id) }
)