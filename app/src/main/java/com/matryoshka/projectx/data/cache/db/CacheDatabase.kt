package com.matryoshka.projectx.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matryoshka.projectx.data.cache.db.interest.CachedInterest
import com.matryoshka.projectx.data.cache.db.interest.InterestCacheDao

const val DATABASE_NAME = "cache"

@Database(entities = [CachedInterest::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestCacheDao
}