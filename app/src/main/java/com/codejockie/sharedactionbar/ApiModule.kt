package com.codejockie.sharedactionbar

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideReceivingService(retrofit: Retrofit): ReceivingService {
        return retrofit.create(ReceivingService::class.java)
    }
}