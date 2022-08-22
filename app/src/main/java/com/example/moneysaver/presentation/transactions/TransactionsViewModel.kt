package com.example.moneysaver.presentation.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moneysaver.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.domain.repository.AccountsRepository
import com.example.moneysaver.domain.repository.CategoriesRepository
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val categoriesRepository: CategoriesRepository,
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    var state by mutableStateOf(TransactionsState())
        private set

     fun loadTransactions() {
            repository.getTransactions()
                .onEach { list ->
                    state = state.copy(
                        transactionList = list,
                        endingBalance = list.sumOf { it.sum }
                    )
                }.launchIn(viewModelScope)
    }

    fun loadTransactionsBetweenDates(minDate: Date, maxDate: Date) {
        repository.getTransactionsInDateRange(minDate, maxDate)
            .onEach { list ->
                state = state.copy(
                    transactionList = list,
                    endingBalance = list.sumOf { it.sum }
                )
            }.launchIn(viewModelScope)
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove previous spending data if transaction isn't new
            repository.getTransactionByUUID(transaction.uuid)?.let {
                val transactionAccount = accountsRepository.getAccountByUUID(it.accountUUID)
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance+it.sum
                )
                // update account
                accountsRepository.insertAccount(updatedAccount)
            }

            // add new spending data
            val transactionAccount = accountsRepository.getAccountByUUID(transaction.accountUUID)
            val updatedAccount = transactionAccount!!.copy(
                balance = transactionAccount.balance-transaction.sum
            )
            accountsRepository.insertAccount(updatedAccount)

            // add or update transaction
            repository.insertTransaction(transaction)
        }
    }

    fun deleteTransactions() {
        viewModelScope.launch {
            repository.deleteAllTransaction()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun loadCategories() {
        categoriesRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list
                )
            }.launchIn(viewModelScope)
    }

    fun loadAccounts() {
        accountsRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    accountsList = list
                )
            }.launchIn(viewModelScope)
    }

    fun getCategoryNameByUUIID(uuid: UUID): String? = runBlocking {
        categoriesRepository.getCategoryByUUID(uuid)?.title
    }

}