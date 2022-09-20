package com.matryoshka.projectx.data.interest

import com.matryoshka.projectx.MattyApiPath
import com.matryoshka.projectx.data.cache.db.interest.InterestsCache
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.exception.throwException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MattyApiInterestsRepo"

@Singleton
class MattyApiInterestsRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val cache: InterestsCache
) : InterestsRepository {

    override suspend fun getAll(): List<Interest> {
        return try {
            cache.getAll {
                httpClient.get(MattyApiPath.GET_INTERESTS_PATH)
                    .body<List<ApiInterest>>()
                    .map { it.toInterest() }
            }
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG)
        }
    }

    override suspend fun getById(id: String): Interest? {
        TODO("Not yet implemented")
    }

    override suspend fun getByIds(ids: List<String>): List<Interest> {
        TODO("Not yet implemented")
    }
}

private data class ApiInterest(
    val id: String,
    val name: String,
    val emoji: String
) {
    fun toInterest() = Interest(id = id, name = name, emoji)
}