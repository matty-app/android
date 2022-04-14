package com.matryoshka.projectx.data.repository

import com.matryoshka.projectx.data.Interest

interface InterestsRepository {
    suspend fun getAll(): List<Interest>
}