package com.matryoshka.projectx.di

import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.service.MattyApiAuthService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthService(implementation: MattyApiAuthService): AuthService
}