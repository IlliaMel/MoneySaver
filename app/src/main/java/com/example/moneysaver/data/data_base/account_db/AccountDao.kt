package com.example.moneysaver.data.data_base.account_db

import androidx.room.*
import com.example.moneysaver.domain.model.Account
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface AccountDao {

    @Query("SELECT * FROM `account` WHERE uuid = :uuid")
    suspend fun getAccountByUUID(uuid: UUID): Account?

    @Query("SELECT * FROM `account` ORDER BY creationDate")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE isForGoal=0 AND isForDebt=0 ORDER BY creationDate")
    fun getSimpleAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE isForGoal=0 ORDER BY creationDate")
    fun getSimpleAndDeptAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE isForDebt=0 ORDER BY creationDate")
    fun getDebtAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE isForGoal=1 ORDER BY creationDate")
    fun getGoalAccounts(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("DELETE FROM `account`")
    suspend fun deleteAll()

}

