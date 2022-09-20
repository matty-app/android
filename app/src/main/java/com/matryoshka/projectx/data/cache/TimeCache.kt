package com.matryoshka.projectx.data.cache

import kotlin.time.Duration

open class TimeCache {
    protected fun calculateExpireTime(lifeSpan: Duration) =
        System.currentTimeMillis() + lifeSpan.inWholeMilliseconds

    protected fun isStaled(expireTime: Long) = expireTime < System.currentTimeMillis()
}