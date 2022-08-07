package com.example.moneysaver.domain.repository

import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow



interface AccountsRepository {

    fun getAccounts(): Flow<List<Account>>

    fun getGoalAccounts(): Flow<List<Account>>

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun deleteAllAccounts()

}