package com.example.myapplication.Repository


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceRepositoryModule {
    @Binds
    abstract fun ServicesRepositoryFirebaseImpl(servicesRepositoryFirebase: ServicesRepositoryFirebase): IOTServicesRepository
}