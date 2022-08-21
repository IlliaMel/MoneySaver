package com.example.moneysaver.domain.repository

import com.example.moneysaver.data.remote.CurrencyDto
import com.example.moneysaver.domain.util.Resource

interface  CurrencyRepository {
    suspend fun getCurrencyData(base: String, symbols: String) : Resource<CurrencyDto>
}