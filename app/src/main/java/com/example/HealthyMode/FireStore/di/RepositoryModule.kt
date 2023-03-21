package com.example.HealthyMode.FireStore.di

import com.example.HealthyMode.FireStore.Repository.Repository
import com.example.HealthyMode.FireStore.Repository.RepositoryImp
import com.google.firebase.firestore.DocumentReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        database: DocumentReference,
    ): Repository {
        return RepositoryImp(database)
    }
}