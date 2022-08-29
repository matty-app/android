package com.matryoshka.projectx.data.cache.db.interest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interests")
data class CachedInterest(
    @PrimaryKey
    val id: String,
    val name: String,
    val emoji: String?
)
