package com.matryoshka.projectx.di

import com.matryoshka.projectx.data.event.EventsRepository
import com.matryoshka.projectx.data.event.FirestoreEventsRepository
import com.matryoshka.projectx.data.image.ImagesRepository
import com.matryoshka.projectx.data.interest.FirestoreInterestsRepository
import com.matryoshka.projectx.data.interest.InterestsRepository
import com.matryoshka.projectx.data.repository.firebase.FirebaseImagesRepository
import com.matryoshka.projectx.data.user.MattyApiUsersRepository
import com.matryoshka.projectx.data.user.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun bindUsersRepository(implementation: MattyApiUsersRepository): UsersRepository

    @Binds
    abstract fun bindInterestsRepository(implementation: FirestoreInterestsRepository): InterestsRepository

    @Binds
    abstract fun bindImagesRepository(implementation: FirebaseImagesRepository): ImagesRepository

    @Binds
    abstract fun bindEventRepository(implementation: FirestoreEventsRepository): EventsRepository
}