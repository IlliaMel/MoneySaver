package com.example.moneysaver.data.repository

import com.example.moneysaver.data.remote.CurrencyApi
import com.example.moneysaver.domain.repository.CurrencyRepository
import com.example.moneysaver.domain.util.Resource
import javax.inject.Inject
import com.example.moneysaver.data.remote.CurrencyDto


class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
): CurrencyRepository {

    override suspend fun getCurrencyData(
        base: String,
        symbols: String
    ): Resource<CurrencyDto> {
        return try {
            Resource.Success(
                data = api.getCurrencyData(
                    base = base, symbols = symbols
                )
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

}