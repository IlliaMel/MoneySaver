package com.example.moneysaver.domain.repository

import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow



interface AccountsRepository {

    fun getAllAccounts(): Flow<List<Account>>

    fun getSimpleAndDeptAccounts(): Flow<List<Account>>

    fun getSimpleAccounts(): Flow<List<Account>>

    fun getDebtAccounts(): Flow<List<Account>>

    fun getGoalAccounts(): Flow<List<Account>>

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun deleteAllAccounts()

}