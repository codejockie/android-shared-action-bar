package com.codejockie.sharedactionbar

import retrofit2.http.Headers
import retrofit2.http.POST

interface ReceivingService {
    @Headers("Accept: application/json")
    @POST("")
    suspend fun getTransactions()
}