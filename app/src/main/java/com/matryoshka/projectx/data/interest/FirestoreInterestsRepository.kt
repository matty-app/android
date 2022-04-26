package com.matryoshka.projectx.data.interest

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.matryoshka.projectx.exception.GetInterestsException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val FIRESTORE_INTERESTS = "interests"

private const val TAG = "FirestoreInterestsRepo"

class FirestoreInterestsRepository @Inject constructor(private val db: FirebaseFirestore) :
    InterestsRepository {

    override suspend fun getAll(): List<Interest> {
        return try {
            db.collection(FIRESTORE_INTERESTS).get().await()
                .map { it.toObject<FirestoreInterest>().toInterest() }
        } catch (ex: Exception) {
            Log.e(TAG, "getInterests: ${ex.message}")
            throw GetInterestsException(ex)
        }
    }

    override suspend fun getById(id: String): Interest? {
        return db.collection(FIRESTORE_INTERESTS)
            .document(id)
            .get()
            .await()
            .toObject<FirestoreInterest>()
            ?.toInterest()
    }

    override suspend fun getByIds(ids: List<String>): List<Interest> {
        return db.collection(FIRESTORE_INTERESTS)
            .whereIn(FieldPath.documentId(), ids)
            .get()
            .await()
            .map {
                it.toObject<FirestoreInterest>().toInterest()
            }

    }
}

data class FirestoreInterest(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val emoji: String? = null
) {
    fun toInterest() = Interest(id = id, name = name, emoji)
}