package com.matryoshka.projectx.service

import android.content.SharedPreferences
import android.util.Log
import com.matryoshka.projectx.MattyApiPath.LOGIN_PATH
import com.matryoshka.projectx.MattyApiPath.REGISTER_PATH
import com.matryoshka.projectx.MattyApiPath.SEND_LOGIN_CODE_PATH
import com.matryoshka.projectx.MattyApiPath.SEND_REGISTER_CODE_PATH
import com.matryoshka.projectx.data.auth.TokensInfo
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
    private val client: HttpClient,
    private val sharedPrefs: SharedPreferences
) : AuthService {
    override suspend fun sendRegistrationCodeToEmail(email: String): Instant {
        val methodName = "sendRegistrationCodeToEmail"
        return try {
            val result = client.get(SEND_REGISTER_CODE_PATH) {
                url {
                    parameters.append("email", email)
                }
            }
            Log.d(TAG, "$methodName: $result")
            result.body<SendCodeResponse>().expiresAt
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG, methodName)
        }
    }

    override suspend fun sendLoginCodeToEmail(email: String): Instant {
        val methodName = "sendLoginCodeToEmail"
        return try {
            val result = client.get(SEND_LOGIN_CODE_PATH) {
                url {
                    parameters.append("email", email)
                }
            }
            Log.d(TAG, "$methodName: $result")
            result.body<SendCodeResponse>().expiresAt
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG, methodName)
        }
    }

    override suspend fun register(email: String, userName: String, verificationCode: Int) {
        val methodName = "register"
        try {
            val result = client.post(REGISTER_PATH) {
                contentType(ContentType.Application.Json)
                setBody(RegisterModel(email, userName, verificationCode))
            }
            val tokensInfo: TokensInfo = result.body()
            sharedPrefs.setTokens(
                accessToken = tokensInfo.accessToken,
                refreshToken = tokensInfo.refreshToken
            )
            Log.d(TAG, "$methodName: $result")
        } catch (ex: UnauthorizedException) {
            throwException(InvalidVerificationCodeException(), TAG, methodName)
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG, methodName)
        }
    }

    override suspend fun login(email: String, verificationCode: Int) {
        val methodName = "login"
        try {
            val tokensInfo: TokensInfo = client.post(LOGIN_PATH) {
                contentType(ContentType.Application.Json)
                setBody(LoginModel(email, verificationCode))
            }.body()
            //val tokensInfo: TokensInfo = result.body()
            sharedPrefs.setTokens(
                accessToken = tokensInfo.accessToken,
                refreshToken = tokensInfo.refreshToken
            )
            //Log.d(TAG, "$methodName: $result")
        } catch (ex: UnauthorizedException) {
            throwException(InvalidVerificationCodeException(), TAG, methodName)
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG, methodName)
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