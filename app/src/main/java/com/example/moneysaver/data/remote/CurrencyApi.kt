package com.example.moneysaver.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/latest")
    suspend fun getCurrencyData(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): CurrencyDto
}