package com.matryoshka.projectx.data.repository.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.data.repository.UsersRepository
import com.matryoshka.projectx.exception.SaveUserException
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

const val FIRESTORE_USERS = "users"

private const val TAG = "FirestoreUsersRepo"

class FirestoreUsersRepository @Inject constructor(private val db: FirebaseFirestore) :
    UsersRepository {

    override suspend fun save(user: User) {
        try {
            db.collection(FIRESTORE_USERS)
                .document(user.uid)
                .set(user.toFirestoreUser(db), SetOptions.merge())
                .await()
        } catch (ex: Exception) {
            Log.e(TAG, "saveUser ${user.uid}: ${ex.message}")
            throw SaveUserException(ex)
        }
    }
}

private data class FirestoreUser(
    @DocumentId
    val uid: String = "",
    val name: String? = null,
    val email: String? = null,
    val interests: List<DocumentReference> = emptyList()
)

private fun User.toFirestoreUser(db: FirebaseFirestore) = FirestoreUser(
    uid = uid,
    name = name,
    email = email,
    interests = interests.map { db.collection(FIRESTORE_INTERESTS).document(it.id) }
)