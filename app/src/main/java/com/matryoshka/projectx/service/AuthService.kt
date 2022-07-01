package com.matryoshka.projectx.service

import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.exception.ProjectxException

interface AuthService {

    suspend fun sendSignInLinkToEmail(email: String)

    fun isSignInWithEmailLink(link: String): Boolean

    suspend fun signInByEmailLink(email: String, link: String): User

    suspend fun signUpByEmailLink(email: String, name: String, link: String): User

    suspend fun checkEmailExists(email: String): Boolean

    fun getCurrentUser(): User?

    suspend fun updateUser(user: User)
}

val AuthService.requireUser: User
    get() = requireNotNull(getCurrentUser()) {
        ProjectxException("User can't be null!")
    }