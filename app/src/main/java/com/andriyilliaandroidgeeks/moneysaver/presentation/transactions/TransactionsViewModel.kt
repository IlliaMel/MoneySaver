package com.andriyilliaandroidgeeks.moneysaver.presentation.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andriyilliaandroidgeeks.moneysaver.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.CategoriesData
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val financeRepository: FinanceRepository,
) : ViewModel() {

    var account by mutableStateOf(AccountsData.allAccountFilter)

    var state by mutableStateOf(TransactionsState())
        private set

     fun loadTransactions() {
         financeRepository.getTransactions()
                .onEach { list ->
                    var resultList = list.filter {  it.accountUUID == account.uuid || (account.isForGoal && account.isForDebt) }
                    state = state.copy(
                        transactionList = resultList,
                        endingBalance = resultList.sumOf { it.sum }
                    )
                }.launchIn(viewModelScope)
    }

    fun loadTransactionsBetweenDates(minDate: Date, maxDate: Date) {
        financeRepository.getTransactionsInDateRange(minDate, maxDate)
            .onEach { list ->
                var resultList = list.filter {  it.accountUUID == account.uuid || (account.isForGoal && account.isForDebt) }
                state = state.copy(
                    transactionList = resultList,
                    endingBalance = resultList.sumOf { it.sum }
                )
            }.launchIn(viewModelScope)
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove previous spending data if transaction isn't new
            financeRepository.getTransactionByUUID(transaction.uuid)?.let {
                val transactionAccount = financeRepository.getAccountByUUID(it.accountUUID)
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance-it.sum
                )
                // update account
                financeRepository.insertAccount(updatedAccount)
            }

            // add new spending data
            val transactionAccount = financeRepository.getAccountByUUID(transaction.accountUUID)
            val updatedAccount = transactionAccount!!.copy(
                balance = transactionAccount.balance+transaction.sum
            )
            financeRepository.insertAccount(updatedAccount)

            // add or update transaction
            financeRepository.insertTransaction(transaction)
        }
    }

    fun deleteTransactions() {
        viewModelScope.launch {
            financeRepository.deleteAllTransaction()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove spending data if transaction isn't new
            val transactionAccount = financeRepository.getAccountByUUID(transaction.accountUUID)
            transactionAccount?.let {
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance-transaction.sum
                )
                // update account
                financeRepository.insertAccount(updatedAccount)
            }

            financeRepository.deleteTransaction(transaction)
        }
    }

    fun loadCategories() {
        financeRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list
                )
            }.launchIn(viewModelScope)
    }

    fun loadAccounts() {
        financeRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    accountsList = list
                )
            }.launchIn(viewModelScope)
    }

    fun getCategoryNameByUUIID(uuid: UUID): String? = runBlocking {
        financeRepository.getCategoryByUUID(uuid)?.title
    }

    fun addingTransactionIsAllowed(): Boolean {
        return state.accountsList.isNotEmpty()
                && state.categoriesList.isNotEmpty()
                && state.categoriesList[0].uuid!= CategoriesData.addCategory.uuid
    }

}