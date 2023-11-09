package com.codejockie.sharedactionbar

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {
    @Binds
    abstract fun bindReceivingRepository(receivingRepository: ReceivingRepoImpl): ReceivingRepository
}