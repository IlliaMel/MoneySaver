package com.example.moneysaver.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moneysaver.data.remote.CurrencyDto

import com.example.moneysaver.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface  CurrencyApiRepository {
    suspend fun getCurrencyData(base: String, symbols: String): Resource<CurrencyDto>
}