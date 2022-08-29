package com.matryoshka.projectx.service

import android.content.SharedPreferences
import android.util.Log
import com.matryoshka.projectx.MattyApiPath.LOGIN_CODE_PATH
import com.matryoshka.projectx.MattyApiPath.LOGIN_PATH
import com.matryoshka.projectx.MattyApiPath.REGISTER_CODE_PATH
import com.matryoshka.projectx.MattyApiPath.REGISTER_PATH
import com.matryoshka.projectx.data.auth.AuthTokens
import com.matryoshka.projectx.data.user.User
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.exception.UnauthorizedException
import com.matryoshka.projectx.exception.throwException
import com.matryoshka.projectx.utils.setTokens
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MattyApiAuthService"

@Singleton
class MattyApiAuthService @Inject constructor(
    private val httpClient: HttpClient,
    private val sharedPrefs: SharedPreferences
) : AuthService {
    override suspend fun sendRegistrationCode(email: String): Instant {
        return try {
            val result = httpClient.get(SEND_REGISTER_CODE_PATH) {
            val response: SendCodeResponse = httpClient.get(REGISTER_CODE_PATH) {
                url {
                    parameters.append("email", email)
                }
            }.body()
            Log.d(TAG, "$REGISTER_CODE_PATH: $response")
            response.expiresAt
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG)
        }
    }

    override suspend fun sendLoginCode(email: String): Instant {
        return try {
            val response: SendCodeResponse = httpClient.get(LOGIN_CODE_PATH) {
                url {
                    parameters.append("email", email)
                }
            }.body()
            Log.d(TAG, "$LOGIN_CODE_PATH: $response")
            response.expiresAt
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG)
        }
    }

    override suspend fun register(email: String, userName: String, verificationCode: Int) {
        try {
            val authTokens: AuthTokens = httpClient.post(REGISTER_PATH) {
                contentType(ContentType.Application.Json)
                setBody(RegisterModel(email, userName, verificationCode))
            }.body()
            sharedPrefs.setTokens(
                accessToken = authTokens.accessToken,
                refreshToken = authTokens.refreshToken
            )
            Log.d(TAG, "$REGISTER_PATH: $authTokens")
        } catch (ex: UnauthorizedException) {
            throwException(InvalidVerificationCodeException(), TAG)
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG)
        }
    }

    override suspend fun login(email: String, verificationCode: Int) {
        try {
            val authTokens: AuthTokens = httpClient.post(LOGIN_PATH) {
                contentType(ContentType.Application.Json)
                setBody(LoginModel(email, verificationCode))
            }.body()
            sharedPrefs.setTokens(
                accessToken = authTokens.accessToken,
                refreshToken = authTokens.refreshToken
            )
            Log.d(TAG, "$LOGIN_PATH: $authTokens")
        } catch (ex: UnauthorizedException) {
            throwException(InvalidVerificationCodeException(), TAG)
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG)
        }
    }

    override val currentUser: User?
        get() = TODO("Not yet implemented")

    override suspend fun sendSignInLinkToEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun isSignInWithEmailLink(link: String) = false

    override fun isChangeEmailLink(link: String) = false

    override suspend fun signInByEmailLink(email: String, link: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun signUpByEmailLink(email: String, name: String, link: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun checkEmailExists(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateEmail(changeEmailLink: String) {
        TODO("Not yet implemented")
    }
}

private data class RegisterModel(val email: String, val fullName: String, val verificationCode: Int)

private data class LoginModel(val email: String, val verificationCode: Int)

private data class SendCodeResponse(val expiresAt: Instant)