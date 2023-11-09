package com.codejockie.sharedactionbar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ReceivingRepository {
    fun getTransactions(): Flow<Unit>
}

class ReceivingRepoImpl @Inject constructor(
    private val receivingService: ReceivingService
): ReceivingRepository {
    override fun getTransactions(): Flow<Unit> {
        return flow{ emit(receivingService.getTransactions()) }
    }
}