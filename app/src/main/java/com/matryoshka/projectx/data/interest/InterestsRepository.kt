package com.matryoshka.projectx.data.interest

import javax.inject.Singleton

@Singleton
interface InterestsRepository {
    suspend fun getAll(): List<Interest>

    suspend fun getById(id: String): Interest?

    suspend fun getByIds(ids: List<String>): List<Interest>
}