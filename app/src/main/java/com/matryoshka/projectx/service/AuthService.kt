package com.matryoshka.projectx.service

import com.matryoshka.projectx.data.User

interface AuthService {

    suspend fun sendSignInLinkToEmail(email: String)

    fun isSignInWithEmailLink(link: String): Boolean

    suspend fun signInByEmailLink(email: String, link: String): User

    suspend fun signUpByEmailLink(email: String, name: String, link: String): User

    suspend fun checkEmailExists(email: String): Boolean

    suspend fun getCurrentUser(): User?

    suspend fun updateUser(user: User)
}