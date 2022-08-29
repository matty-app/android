package com.matryoshka.projectx.service

import com.matryoshka.projectx.R
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.exception.AppException
import java.time.Instant
import javax.inject.Singleton

interface AuthService {
    suspend fun sendRegistrationCode(email: String): Instant

    suspend fun sendLoginCode(email: String): Instant

    suspend fun register(email: String, userName: String, verificationCode: Int)

    suspend fun login(email: String, verificationCode: Int)

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
        AppException("User can't be null!")
    }

class InvalidVerificationCodeException : AppException(R.string.invalid_code)