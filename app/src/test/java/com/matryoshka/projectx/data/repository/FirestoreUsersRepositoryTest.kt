package com.matryoshka.projectx.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.matryoshka.projectx.data.User
import com.matryoshka.projectx.data.repository.firestore.FIRESTORE_USERS
import com.matryoshka.projectx.data.repository.firestore.FirestoreUsersRepository
import com.matryoshka.projectx.exception.SaveUserException
import com.matryoshka.projectx.support.taskMock
import com.matryoshka.projectx.support.voidObject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class FirestoreUsersRepositoryTest {
    val user = User(uid = "1", name = "John", email = "john@gmail.com")

    @Test
    fun `should save user`() = runTest {
        val document = mockk<DocumentReference>().apply {
            every { set(any(), any()) } returns taskMock(voidObject)
        }
        val collectionReference = mockk<CollectionReference>().apply {
            every { document(any()) } returns document
        }
        val firestore = mockk<FirebaseFirestore>().apply {
            every { collection(any()) } returns collectionReference
        }
        val repository = FirestoreUsersRepository(firestore)

        repository.save(user)

        verify { firestore.collection(FIRESTORE_USERS) }
        verify { collectionReference.document(user.uid) }
        verify { document.set(any(), SetOptions.merge()) }
    }

    @Test
    fun `should throw SaveUserException if error occurred`() = runTest {
        val firestore = mockk<FirebaseFirestore>().apply {
            coEvery { collection(any()) } throws Exception()
        }
        val repository = FirestoreUsersRepository(firestore)

        assertFailsWith<SaveUserException> { repository.save(user) }
    }
}