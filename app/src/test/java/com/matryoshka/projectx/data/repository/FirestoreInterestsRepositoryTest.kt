package com.matryoshka.projectx.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.matryoshka.projectx.data.Interest
import com.matryoshka.projectx.data.repository.firestore.FIRESTORE_INTERESTS
import com.matryoshka.projectx.data.repository.firestore.FirestoreInterest
import com.matryoshka.projectx.data.repository.firestore.FirestoreInterestsRepository
import com.matryoshka.projectx.exception.GetInterestsException
import com.matryoshka.projectx.support.mockIterable
import com.matryoshka.projectx.support.taskMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class FirestoreInterestsRepositoryTest {
    private val interests = listOf(
        Interest("coding", "Coding"),
        Interest("sport", "Sport")
    )

    @Test
    fun `should get interests`() = runTest {
        val documents = listOf(
            mockk<QueryDocumentSnapshot>().apply {
                every { toObject<FirestoreInterest>().toInterest() } returns interests[0]
            },
            mockk<QueryDocumentSnapshot>().apply {
                every { toObject<FirestoreInterest>().toInterest() } returns interests[1]
            }
        )
        val task = taskMock(mockIterable<QueryDocumentSnapshot, QuerySnapshot>(documents))
        val firestore = mockk<FirebaseFirestore>().apply {
            coEvery { collection(any()).get() } returns task
        }
        val repository = FirestoreInterestsRepository(firestore)

        val results = repository.getAll()

        verify { firestore.collection(FIRESTORE_INTERESTS) }
        assertEquals(interests.size, results.size)
    }

    @Test
    fun `should throw GetInterestsException if error occurred`() = runTest {
        val firestore = mockk<FirebaseFirestore>().apply {
            coEvery { collection(any()).get() } throws Exception()
        }
        val repository = FirestoreInterestsRepository(firestore)

        assertFailsWith<GetInterestsException> { repository.getAll() }
    }
}