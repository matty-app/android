package com.matryoshka.projectx.data.cache.db.interest

import android.content.Context
import android.util.Log
import com.matryoshka.projectx.SharedPrefsKey.PREF_INTERESTS_CACHE_EXPIRE
import com.matryoshka.projectx.data.cache.TimeCache
import com.matryoshka.projectx.data.cache.db.CacheDatabase
import com.matryoshka.projectx.data.interest.Interest
import com.matryoshka.projectx.utils.sharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

private val INTERESTS_CACHE_LIFE_SPAN = 1.days

private const val TAG = "InterestsCache"

@Singleton
class InterestsCache @Inject constructor(
    db: CacheDatabase,
    @ApplicationContext private val context: Context
) : TimeCache() {
    private val interestsDao = db.interestDao()

    private var expireTime: Long = 0

    suspend fun getAll(
        forceReload: Boolean = false,
        onReload: suspend () -> List<Interest>
    ): List<Interest> {
        if (forceReload) return reload(onReload)

        if (expireTime == 0L)
            expireTime = context.sharedPreferences.getLong(PREF_INTERESTS_CACHE_EXPIRE, 0)

        return if (isStaled(expireTime)) reload(onReload)
        else {
            val interests = interestsDao.getAll().map { it.toDomain() }
            Log.d(TAG, "get interests from cache (${interests.size} items)")
            interests
        }
    }

    suspend fun save(interests: Collection<Interest>) {
        expireTime = calculateExpireTime(INTERESTS_CACHE_LIFE_SPAN)
        context.sharedPreferences.edit().putLong(PREF_INTERESTS_CACHE_EXPIRE, expireTime).apply()
        interestsDao.deleteAndAddAll(interests.map { it.toCachedInterest() })
    }

    private suspend fun reload(onReload: suspend () -> List<Interest>): List<Interest> {
        val interests = onReload()
        if (interests.isNotEmpty()) {
            save(interests)
        }
        Log.d(TAG, "get interests from source (${interests.size} items)")
        return interests
    }
}

private fun Interest.toCachedInterest() = CachedInterest(
    id = id,
    name = name,
    emoji = emoji
)

private fun CachedInterest.toDomain() = Interest(
    id = id,
    name = name,
    emoji = emoji
)