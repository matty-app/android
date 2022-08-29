package com.matryoshka.projectx.data.cache

enum class CacheExpireTimeUnit {
    SECONDS,
    MINUTES,
    HOURS,
    DAYS
}

fun getExpiredPeriodInMillis(expiredPeriod: Long, expiredTimeUnit: CacheExpireTimeUnit): Long =
    when (expiredTimeUnit) {
        CacheExpireTimeUnit.SECONDS -> expiredPeriod * 1000L
        CacheExpireTimeUnit.MINUTES -> expiredPeriod * 60 * 1000L
        CacheExpireTimeUnit.HOURS -> expiredPeriod * 60 * 60 * 1000L
        CacheExpireTimeUnit.DAYS -> expiredPeriod * 24 * 60 * 60 * 1000L
    }