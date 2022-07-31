package com.example.moneysaver.data.data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneysaver.domain.transaction.Transaction

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