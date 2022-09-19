package com.matryoshka.projectx.data.repository

import com.matryoshka.projectx.data.user.User
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FirestoreUsersRepositoryTest {
    private val user = User(id = "1", name = "John", email = "john@gmail.com")

/*    @Test
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
        val repository = FirestoreUsersRepository(firestore, mockk(relaxed = true))

        repository.save(user)

        verify { firestore.collection(FIRESTORE_USERS) }
        verify { collectionReference.document(user.id) }
        verify { document.set(any(), SetOptions.merge()) }
    }*/

/*    @Test
    fun `should throw SaveUserException if error occurred`() = runTest {
        val firestore = mockk<FirebaseFirestore>().apply {
            coEvery { collection(any()) } throws Exception()
        }
        val repository = FirestoreUsersRepository(firestore, mockk(relaxed = true))

        assertFailsWith<SaveUserException> { repository.save(user) }
    }*/
}