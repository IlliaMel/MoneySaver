package com.andriyilliaandroidgeeks.moneysaver.data.data_base.currency_db

import androidx.room.*
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM `currency`")
    fun getCurrencyTypes(): Flow<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyType(currency: Currency)

    @Query("DELETE FROM `currency`")
    suspend fun deleteAll()
}

