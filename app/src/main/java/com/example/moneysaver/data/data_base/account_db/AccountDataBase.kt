package com.example.moneysaver.data.data_base.account_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneysaver.data.data_base.Converters
import com.example.moneysaver.domain.account.Account

@Database(
    entities = [Account::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AccountDataBase: RoomDatabase() {
    abstract val accountDao: AccountDao

    companion object {
        const val DATABASE_NAME = "accounts_db"
    }
}

