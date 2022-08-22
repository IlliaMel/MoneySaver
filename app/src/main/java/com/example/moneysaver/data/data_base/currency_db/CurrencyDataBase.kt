package com.example.moneysaver.data.data_base.currency_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneysaver.data.data_base.Converters

import com.example.moneysaver.domain.currency.Currency


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