package com.matryoshka.projectx.data.repository.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.matryoshka.projectx.data.Interest
import com.matryoshka.projectx.data.repository.InterestsRepository
import com.matryoshka.projectx.exception.GetInterestsException
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

const val FIRESTORE_INTERESTS = "interests"

private const val TAG = "FirestoreInterestsRepos"

class FirestoreInterestsRepository @Inject constructor(private val db: FirebaseFirestore) :
    InterestsRepository {

    override suspend fun getAll(): List<Interest> {
        try {
            return db.collection(FIRESTORE_INTERESTS).get().await()
                .map { it.toObject<FirestoreInterest>().toInterest() }
        } catch (ex: Exception) {
            Log.e(TAG, "getInterests: ${ex.message}")
            throw GetInterestsException(ex)
        }
    }
}

data class FirestoreInterest(
    @DocumentId
    val id: String = "",
    val name: String = ""
) {
    fun toInterest() = Interest(id = id, name = name)
}