package com.example.moneysaver.domain.repository

import com.example.moneysaver.data.remote.CurrencyDto
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FinanceRepository {

    // Transactions
    suspend fun getTransactionByUUID(uuid: UUID): Transaction?

    fun getTransactions(): Flow<List<Transaction>>

    fun getTransactionsByCategoryUUID(categoryUUID: UUID): Flow<List<Transaction>>

    fun getTransactionsByAccountUUID(accountUUID: UUID): Flow<List<Transaction>>

    fun getTransactionsInDateRange(minDate: Date, maxDate: Date): Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun deleteAllTransaction()


    // Categories
    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryByUUID(uuid: UUID): Category?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun deleteAllCategories()

    // Accounts
    suspend fun getAccountByUUID(uuid: UUID): Account?

    fun getAllAccounts(): Flow<List<Account>>

    fun getSimpleAndDeptAccounts(): Flow<List<Account>>

    fun getSimpleAccounts(): Flow<List<Account>>

    fun getDebtAccounts(): Flow<List<Account>>

    fun getGoalAccounts(): Flow<List<Account>>

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun deleteAllAccounts()


    // Currency
    fun getCurrencyTypes(): Flow<List<Currency>>

    suspend fun insertCurrencyType(currencyType: Currency)

    suspend fun deleteAll()

    // Currency Api Data
    suspend fun getCurrencyData(base: String, symbols: String): Resource<CurrencyDto>

}