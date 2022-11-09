package com.andriyilliaandroidgeeks.moneysaver.data.data_base.currency_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andriyilliaandroidgeeks.moneysaver.data.data_base.Converters

import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency


@Database(
    entities = [Currency::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CurrencyDataBase: RoomDatabase() {
    abstract val currencyDao: CurrencyDao

    companion object {
        const val DATABASE_NAME = "currency_db"
    }
}