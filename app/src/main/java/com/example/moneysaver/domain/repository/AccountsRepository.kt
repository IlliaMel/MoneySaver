package com.example.moneysaver.domain.repository

import androidx.room.Query
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*


interface AccountsRepository {

    suspend fun getAccountByUUID(uuid: UUID): Account?

    fun getAllAccounts(): Flow<List<Account>>

    fun getSimpleAndDeptAccounts(): Flow<List<Account>>

    fun getSimpleAccounts(): Flow<List<Account>>

    fun getDebtAccounts(): Flow<List<Account>>

    fun getGoalAccounts(): Flow<List<Account>>

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun deleteAllAccounts()

}