package com.example.moneysaver.data.data_base.account_db

import androidx.room.*
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface AccountDao {



    @Query("SELECT * FROM `account` WHERE isForGoal=0 AND isForDebt=0")
    fun getSimpleAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE isForGoal=0")
    fun getAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE isForGoal=1")
    fun getGoalAccounts(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("DELETE FROM `account`")
    suspend fun deleteAll()

}

