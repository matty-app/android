package com.matryoshka.projectx.di

import com.matryoshka.projectx.data.event.EventsRepository
import com.matryoshka.projectx.data.event.FirestoreEventsRepository
import com.matryoshka.projectx.data.image.ImagesRepository
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.data.interest.MattyApiInterestsRepository
import com.matryoshka.projectx.data.repository.firebase.FirebaseImagesRepository
import com.matryoshka.projectx.data.user.MattyApiUsersRepository
import com.matryoshka.projectx.data.user.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindUsersRepository(implementation: MattyApiUsersRepository): UsersRepository

    @Binds
    @Singleton
    abstract fun bindInterestsRepository(implementation: MattyApiInterestsRepository): InterestsRepository

    @Binds
    @Singleton
    abstract fun bindImagesRepository(implementation: FirebaseImagesRepository): ImagesRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(implementation: FirestoreEventsRepository): EventsRepository
}