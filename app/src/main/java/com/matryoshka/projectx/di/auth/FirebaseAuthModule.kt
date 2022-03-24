package com.matryoshka.projectx.di.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class FirebaseAuthModule {

    @Provides
    fun provideFirebaseAuth() = Firebase.auth
}