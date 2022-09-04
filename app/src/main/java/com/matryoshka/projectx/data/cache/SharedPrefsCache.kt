package com.matryoshka.projectx.data.cache

import android.content.Context
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.matryoshka.projectx.utils.gson
import com.matryoshka.projectx.utils.sharedPreferences
import java.lang.ref.WeakReference
import java.lang.reflect.Type
import kotlin.time.Duration

private const val TAG = "SharedPrefsCache"

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
class SharedPrefsCache<T> private constructor(
    private val cacheContainerType: Type,
    private val key: String,
    private val lifeSpan: Duration
) {
    private var expireTime: Long = 0
    private var objRef = WeakReference<T?>(null)

    suspend fun get(
        context: Context,
        forceReload: Boolean = false,
        onReload: suspend () -> T?
    ): T? {
        if (forceReload) return onReload()

        val obj = objRef.get()
        if (obj != null)
            return if (expireTime > System.currentTimeMillis()) obj
            else reload(context, onReload)

        val json = context.sharedPreferences.getString(key, null) ?: return reload(context, onReload)

        val cacheContainer = gson.fromJson<CacheContainer<T>>(json, cacheContainerType)
        if (cacheContainer.expireTime < System.currentTimeMillis())
            return reload(context, onReload)

        val cachedObject = cacheContainer.obj
        expireTime = cacheContainer.expireTime
        objRef = WeakReference(cachedObject)
        Log.d(TAG, "get $key from cache: $cachedObject")
        return cachedObject
    }

    fun save(context: Context, targetObject: T) {
        val expireTime =
            System.currentTimeMillis() + lifeSpan.inWholeMilliseconds
        val cacheContainer = CacheContainer(expireTime, targetObject)

        this.expireTime = expireTime
        this.objRef = WeakReference(targetObject)
        val json = gson.toJson(cacheContainer)
        context.sharedPreferences.edit().putString(key, json).apply()
    }

    private suspend fun reload(context: Context, onReload: suspend () -> T?): T? {
        val targetObject = onReload()
        if (targetObject != null) {
            save(context, targetObject)
        }
        Log.d(TAG, "get $key from source: $targetObject")
        return targetObject
    }

    companion object {
        inline operator fun <reified T> invoke(
            key: String,
            lifeSpan: Duration,
        ) = SharedPrefsCache<T>(
            cacheContainerType = TypeToken.getParameterized(
                CacheContainer::class.java,
                T::class.java
            ).type,
            key = key,
            lifeSpan = lifeSpan,
        )
    }
}

data class CacheContainer<out T>(
    val expireTime: Long,
    val obj: T
)