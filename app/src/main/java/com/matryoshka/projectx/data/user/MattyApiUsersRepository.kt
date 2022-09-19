package com.matryoshka.projectx.data.user

import android.content.Context
import com.matryoshka.projectx.MattyApiPath
import com.matryoshka.projectx.SharedPrefsKey
import com.matryoshka.projectx.data.cache.SharedPrefsCache
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.exception.AppException
import com.matryoshka.projectx.exception.ForbiddenException
import com.matryoshka.projectx.exception.UnauthorizedException
import com.matryoshka.projectx.exception.UserSignedOutException
import com.matryoshka.projectx.exception.throwException
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours

private const val TAG = "MattyApiUsersRepository"

@Singleton
class MattyApiUsersRepository @Inject constructor(
    private val httpClient: HttpClient,
    @ApplicationContext private val context: Context
) : UsersRepository {

    private val userCache = SharedPrefsCache<User>(
        key = SharedPrefsKey.PREF_USER_CACHE,
        lifeSpan = 1.hours,
    )

    override suspend fun getCurrent(): User? {
        return try {
            userCache.get(context = context) {
                httpClient.get(MattyApiPath.GET_CURRENT_USER_PATH).body<ApiUser>().toDomain()
            }
        } catch (ex: UnauthorizedException) {
            throwException(UserSignedOutException(), TAG)
        } catch (ex: ForbiddenException) {
            throwException(UserSignedOutException(), TAG)
        } catch (ex: Exception) {
            throwException(AppException(ex), TAG)
        }
    }

    override suspend fun save(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String, flat: Boolean): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getByIds(ids: List<String>): List<User> {
        TODO("Not yet implemented")
    }
}

private data class ApiUser(
    val id: String,
    val fullName: String,
    val email: String,
    val interests: List<String>
)

private fun ApiUser.toDomain() = User(
    id = id,
    name = fullName,
    email = email,
    interests = interests.map { Interest(id = "", name = it) }
)