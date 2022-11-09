package com.andriyilliaandroidgeeks.moneysaver.data.data_base.transaction_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andriyilliaandroidgeeks.moneysaver.data.data_base.Converters
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TransactionDataBase: RoomDatabase() {
    abstract val transactionDao: TransactionDao

    companion object {
        const val DATABASE_NAME = "transactions_db"
    }
}