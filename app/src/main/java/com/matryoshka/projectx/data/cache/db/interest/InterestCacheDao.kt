package com.matryoshka.projectx.data.cache.db.interest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface InterestCacheDao {

    @Query("SELECT * FROM interests")
    suspend fun getAll(): List<CachedInterest>

    @Transaction
    suspend fun deleteAndAddAll(interests: Collection<CachedInterest>) {
        deleteAll()
        addAll(interests)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(interests: Collection<CachedInterest>)

    @Query("DELETE FROM interests")
    suspend fun deleteAll()
}