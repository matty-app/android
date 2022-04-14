package com.matryoshka.projectx.di

import com.matryoshka.projectx.data.repository.InterestsRepository
import com.matryoshka.projectx.data.repository.UsersRepository
import com.matryoshka.projectx.data.repository.firestore.FirestoreInterestsRepository
import com.matryoshka.projectx.data.repository.firestore.FirestoreUsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun bindUsersRepository(implementation: FirestoreUsersRepository): UsersRepository

    @Binds
    abstract fun bindInterestsRepository(implementation: FirestoreInterestsRepository): InterestsRepository
}