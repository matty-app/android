package com.matryoshka.projectx.service

import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.exception.ProjectxException
import javax.inject.Singleton


@Singleton
interface AuthService {
    val currentUser: User?

    suspend fun sendSignInLinkToEmail(email: String)

    fun isSignInWithEmailLink(link: String): Boolean

    fun isChangeEmailLink(link: String): Boolean

    suspend fun signInByEmailLink(email: String, link: String): User

    suspend fun signUpByEmailLink(email: String, name: String, link: String): User

    suspend fun checkEmailExists(email: String): Boolean

    suspend fun updateUser(user: User)

    suspend fun updateEmail(changeEmailLink: String)
}

val AuthService.requireUser: User
    get() = requireNotNull(currentUser) {
        ProjectxException("User can't be null!")
    }