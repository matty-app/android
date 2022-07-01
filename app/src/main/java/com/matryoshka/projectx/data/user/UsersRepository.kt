package com.matryoshka.projectx.data.user

interface UsersRepository {
    suspend fun save(user: User)

    suspend fun getById(id: String, flat: Boolean): User?

    suspend fun getByIds(ids: List<String>): List<User>
}