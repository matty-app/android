package com.matryoshka.projectx.data.repository

import com.matryoshka.projectx.data.User

interface UsersRepository {
    suspend fun save(user: User)
}