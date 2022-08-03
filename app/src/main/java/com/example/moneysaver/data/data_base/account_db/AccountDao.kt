package com.example.moneysaver.data.data_base.account_db

import androidx.room.*
import com.example.moneysaver.domain.account.Account
import kotlinx.coroutines.flow.Flow


@Dao
interface AccountDao {
    @Query("SELECT * FROM `account`")
    fun getAccounts(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("DELETE FROM `account`")
    suspend fun deleteAll()

}

