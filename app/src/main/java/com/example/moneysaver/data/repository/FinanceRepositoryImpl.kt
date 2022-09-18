package com.example.moneysaver.data.repository

import androidx.room.Query
import com.example.moneysaver.data.data_base.account_db.AccountDao
import com.example.moneysaver.data.data_base.category_db.CategoryDao
import com.example.moneysaver.data.data_base.currency_db.CurrencyDao
import com.example.moneysaver.data.data_base.transaction_db.TransactionDao
import com.example.moneysaver.data.remote.CurrencyApi
import com.example.moneysaver.data.remote.CurrencyDto
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.repository.FinanceRepository
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.*

class FinanceRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao,
    private val currencyDao: CurrencyDao,
    private val currencyApi: CurrencyApi
) : FinanceRepository {

    // Transactions
    override suspend fun getTransactionByUUID(uuid: UUID): Transaction? {
        return transactionDao.getTransactionByUUID(uuid)
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getTransactions()
    }

    override fun getTransactionsByCategoryUUID(categoryUUID: UUID): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategoryUUID(categoryUUID)
    }

    override fun getTransactionsByAccountUUID(accountUUID: UUID): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByAccountUUID(accountUUID)
    }

    override fun getTransactionsByToAccountUUID(toAccountUUID: UUID): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByToAccountUUID(toAccountUUID)
    }

    override fun getTransactionsInDateRange(minDate: Date, maxDate: Date): Flow<List<Transaction>> {
        return transactionDao.getTransactionsInDateRange(minDate, maxDate)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        return transactionDao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun deleteAllTransaction() {
        transactionDao.deleteAll()
    }

    override suspend fun deleteTransactionsByAccountUUID(accountUUID: UUID){
        transactionDao.deleteTransactionsByAccountUUID(accountUUID)
    }

    override suspend fun deleteTransactionsByToAccountUUID(toAccountUUID: UUID) {
        transactionDao.deleteTransactionsByToAccountUUID(toAccountUUID)
    }

    override suspend fun deleteTransactionsByCategoryUUID(categoryUUID: UUID) {
        transactionDao.deleteTransactionsByCategoryUUID(categoryUUID)
    }


    // Categories
    override  fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories()
    }

    override fun getCategories(isForSpendings: Boolean): Flow<List<Category>> {
        return categoryDao.getCategories(isForSpendings)
    }

    override suspend fun getCategoryByUUID(uuid: UUID): Category? {
        return categoryDao.getCategoryByUUID(uuid)
    }

    override suspend fun insertCategory(category: Category) {
        return categoryDao.insertCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAll()
    }


    // Accounts
    override suspend fun getAccountByUUID(uuid: UUID): Account? {
        return accountDao.getAccountByUUID(uuid)
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts()
    }

    override fun getSimpleAndDeptAccounts(): Flow<List<Account>> {
        return accountDao.getSimpleAndDeptAccounts()
    }
    override fun getSimpleAccounts(): Flow<List<Account>> {
        return accountDao.getSimpleAccounts()
    }

    override fun getDebtAccounts(): Flow<List<Account>> {
        return accountDao.getDebtAccounts()
    }

    override fun getGoalAccounts(): Flow<List<Account>> {
        return accountDao.getGoalAccounts()
    }

    override suspend fun insertAccount(account: Account) {
        return accountDao.insertAccount(account)
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }

    override suspend fun deleteAllAccounts() {
        accountDao.deleteAll()
    }


    // Currency
    override fun getCurrencyTypes(): Flow<List<Currency>> {
        return currencyDao.getCurrencyTypes()
    }

    override suspend fun insertCurrencyType(currencyType: Currency){
        currencyDao.insertCurrencyType(currencyType)
    }

    override suspend fun deleteAll(){
        currencyDao.deleteAll()
    }


    // Currency Api Data
    override suspend fun getCurrencyData(
        base: String,
        symbols: String
    ): Resource<CurrencyDto> {
        return try {
            Resource.Success(
                data = currencyApi.getCurrencyData(
                    base = base, symbols = symbols
                )
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

}

