package com.matryoshka.projectx.data.user

import javax.inject.Singleton

@Singleton
interface UsersRepository {
    suspend fun getCurrent(): User?

    suspend fun save(user: User)

    suspend fun getById(id: String, flat: Boolean): User?

    suspend fun getByIds(ids: List<String>): List<User>
}