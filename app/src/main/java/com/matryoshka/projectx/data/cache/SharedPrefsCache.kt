package com.matryoshka.projectx.data.cache

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.matryoshka.projectx.utils.sharedPreferences

private const val TAG = "SharedPrefsCache"

class SharedPrefsCache<T>(
    private val objectClass: Class<T>,
    private val key: String,
    private val expiredPeriod: Long,
    private val expiredTimeUnit: CacheExpireTimeUnit
) {
    suspend fun get(
        context: Context,
        forceReload: Boolean = false,
        onReload: suspend () -> T?
    ): T? {
        val json = context.sharedPreferences.getString(key, null)
        if (forceReload || json == null)
            return reload(context, onReload)

        val cacheContainer = Gson().fromJson(json, CacheContainer::class.java)
        if (cacheContainer.expireTime < System.currentTimeMillis())
            return reload(context, onReload)

        val cachedObject = Gson().fromJson(cacheContainer.objectJson, objectClass)
        Log.d(TAG, "get $objectClass from cache: $cachedObject")
        return cachedObject
    }

    fun save(context: Context, targetObject: T) {
        val expireTime =
            System.currentTimeMillis() + getExpiredPeriodInMillis(expiredPeriod, expiredTimeUnit)
        val cacheContainer = CacheContainer(expireTime, Gson().toJson(targetObject))
        val json = Gson().toJson(cacheContainer)
        context.sharedPreferences.edit().putString(key, json).apply()
    }

    private suspend fun reload(context: Context, onReload: suspend () -> T?): T? {
        val targetObject = onReload()
        if (targetObject != null) {
            save(context, targetObject)
        }
        Log.d(TAG, "get $objectClass from source: $targetObject")
        return targetObject
    }
}

enum class CacheExpireTimeUnit {
    SECONDS,
    MINUTES,
    HOURS,
    DAYS
}

data class CacheContainer(
    val expireTime: Long,
    val objectJson: String
)

fun getExpiredPeriodInMillis(expiredPeriod: Long, expiredTimeUnit: CacheExpireTimeUnit): Long =
    when (expiredTimeUnit) {
        CacheExpireTimeUnit.SECONDS -> expiredPeriod * 1000L
        CacheExpireTimeUnit.MINUTES -> expiredPeriod * 60 * 1000L
        CacheExpireTimeUnit.HOURS -> expiredPeriod * 60 * 60 * 1000L
        CacheExpireTimeUnit.DAYS -> expiredPeriod * 24 * 60 * 60 * 1000L
    }