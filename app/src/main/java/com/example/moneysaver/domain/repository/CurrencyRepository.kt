package com.example.moneysaver.domain.repository

import com.example.moneysaver.data.remote.CurrencyDto
import com.example.moneysaver.domain.currency.Currency
import com.example.moneysaver.domain.util.Resource
import kotlinx.coroutines.flow.Flow


interface  CurrencyRepository {
    fun getCurrencyTypes(): Flow<List<Currency>>

    suspend fun insertCurrencyType(currencyType: Currency)

    suspend fun deleteAll()
}