package com.example.moneysaver.presentation.categories

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.CategoriesData
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.repository.FinanceRepository
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.presentation.MainActivity.Companion.isCategoriesParsed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {



    var account by mutableStateOf(AccountsData.allAccountFilter)

    var state by mutableStateOf(CategoriesState())
        private set

    init {
        loadCategories()
        loadCurrencyData()
        isCategoriesParsed = true
    }

    private fun loadCurrencyData() {
        financeRepository.getCurrencyTypes()
            .onEach { list ->
                state = state.copy(
                    currenciesList = list,
                )
                AccountsData.currenciesList = list
            }.launchIn(viewModelScope)
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.insertCategory(category)
        }
    }
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.getTransactionsByCategoryUUID(category.uuid).onEach { list ->
                for(transaction in list) deleteTransaction(transaction)
            }.launchIn(viewModelScope)
            financeRepository.deleteCategory(category)
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

    fun loadCategories() {
        financeRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list as MutableList<Category>,
                )
            }.launchIn(viewModelScope)
    }

    fun loadCategoriesData() {

        financeRepository.getTransactions().onEach { transactions ->

            val categories = state.categoriesList
            val tempList = mutableMapOf<Category, Double>()
            for(category in categories) {
                var categorySum = 0.0
                for(tr in transactions) {
                    if(tr.accountUUID == account.uuid || (account.isForGoal && account.isForDebt))
                    if(tr.categoryUUID == category.uuid) {
                        categorySum-=tr.sum
                    }
                }
                tempList[category] = categorySum
            }

            state = state.copy(
                categoriesList = categories,
                categoriesSums = tempList
            )

        }.launchIn(viewModelScope)

    }

    fun addingTransactionIsAllowed(): Boolean {
        return state.accountsList.isNotEmpty()
                && state.categoriesList.isNotEmpty()
                && state.categoriesList[0].uuid!= CategoriesData.addCategory.uuid
    }

    fun loadCategoriesDataInDateRange(minDate: Date, maxDate: Date) {

        financeRepository.getTransactionsInDateRange(minDate, maxDate).onEach { transactions ->

            val categories = state.categoriesList
            val tempList = mutableMapOf<Category, Double>()
            for(category in categories) {
                var categorySum = 0.0
                for(tr in transactions) {
                    if(tr.accountUUID == account.uuid || (account.isForGoal && account.isForDebt))
                    if(tr.categoryUUID == category.uuid) {
                        categorySum-=tr.sum
                    }
                }
                tempList[category] = categorySum
            }

            state = state.copy(
                categoriesList = categories,
                categoriesSums = tempList
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

}