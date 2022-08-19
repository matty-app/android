package com.matryoshka.projectx.di

import android.content.SharedPreferences
import android.util.Log
import com.matryoshka.projectx.BuildConfig.MATTY_API_URL
import com.matryoshka.projectx.MattyApiPath.LOGIN_PATH
import com.matryoshka.projectx.MattyApiPath.REFRESH_TOKENS_PATH
import com.matryoshka.projectx.MattyApiPath.REGISTER_PATH
import com.matryoshka.projectx.data.auth.TokensInfo
import com.matryoshka.projectx.exception.BadRequestException
import com.matryoshka.projectx.exception.ForbiddenException
import com.matryoshka.projectx.exception.HttpError
import com.matryoshka.projectx.exception.HttpException
import com.matryoshka.projectx.exception.InternalServerErrorException
import com.matryoshka.projectx.exception.NotFoundException
import com.matryoshka.projectx.exception.NotImplementedException
import com.matryoshka.projectx.exception.ServiceUnavailableException
import com.matryoshka.projectx.exception.UnauthorizedException
import com.matryoshka.projectx.utils.accessToken
import com.matryoshka.projectx.utils.refreshToken
import com.matryoshka.projectx.utils.registerAdapters
import com.matryoshka.projectx.utils.setTokens
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.gson.gson

@InstallIn(SingletonComponent::class)
@Module
object ApiClientModule {

    @Provides
    fun provideMattyApiClient(sharedPrefs: SharedPreferences): HttpClient {
        val client = HttpClient(CIO) {
            install(Logging) {
                logger = AppHttpLogger
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                gson() {
                    registerAdapters()
                }
            }

            defaultRequest {
                url(MATTY_API_URL)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            sharedPrefs.accessToken ?: "",
                            sharedPrefs.refreshToken ?: ""
                        )
                    }

                    refreshTokens {
                        if (shouldRefreshTokens(this.response)) {
                            val result = client.get(REFRESH_TOKENS_PATH) {
                                url {
                                    parameters.append(
                                        "refresh_token",
                                        sharedPrefs.refreshToken ?: ""
                                    )
                                }
                                markAsRefreshTokenRequest()
                            }
                            val tokensInfo: TokensInfo = result.body()
                            val accessToken = tokensInfo.accessToken
                            val refreshToken = tokensInfo.refreshToken
                            sharedPrefs.setTokens(accessToken, refreshToken)
                            BearerTokens(accessToken, refreshToken)
                        } else {
                            //to avoid hanging on repeated 401
                            client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
                                .firstOrNull()!!.clearToken()
                            throwHttpException(response)
                        }
                    }
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        throwHttpException(response)
                    }
                }
            }
        }
        return client
    }
}

private fun shouldRefreshTokens(response: HttpResponse): Boolean {
    val url = response.call.request.url
    val conditions = listOf(
        { url.encodedPath.endsWith("/$LOGIN_PATH") },
        { url.encodedPath.endsWith("/$REGISTER_PATH") }
    )
    return conditions.none { it() }
}

private suspend fun throwHttpException(response: HttpResponse): Nothing {
    val body: Any = response.body()
    val error = if (body is HttpError) {
        body.error
    } else ""
    when (response.status) {
        HttpStatusCode.Unauthorized -> throw UnauthorizedException(error)
        HttpStatusCode.BadRequest -> throw BadRequestException(error)
        HttpStatusCode.Forbidden -> throw ForbiddenException(error)
        HttpStatusCode.NotFound -> throw NotFoundException(error)
        HttpStatusCode.InternalServerError -> throw InternalServerErrorException(error)
        HttpStatusCode.NotImplemented -> throw NotImplementedException(error)
        HttpStatusCode.ServiceUnavailable -> throw ServiceUnavailableException(error)
        else -> throw HttpException(error)
    }
}

private object AppHttpLogger : Logger {
    private const val logTag = "AppHttpLogger"

    override fun log(message: String) {
        Log.i(logTag, message)
    }
}