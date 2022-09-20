package com.matryoshka.projectx.di

import android.content.Context
import androidx.room.Room
import com.matryoshka.projectx.data.cache.db.CacheDatabase
import com.matryoshka.projectx.data.cache.db.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): CacheDatabase {
        return Room.databaseBuilder(
            appContext,
            CacheDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}