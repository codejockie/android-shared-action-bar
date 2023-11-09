package com.codejockie.sharedactionbar

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ClassModule {

    @Provides
    fun provideReceivingRepositoryImpl(receivingService: ReceivingService): ReceivingRepoImpl {
        return ReceivingRepoImpl(receivingService)
    }
}