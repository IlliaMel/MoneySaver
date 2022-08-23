package com.example.moneysaver.presentation.categories

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.data.data_base._test_data.CategoriesData
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.repository.AccountsRepository
import com.example.moneysaver.domain.repository.CategoriesRepository
import com.example.moneysaver.domain.repository.TransactionRepository
import com.example.moneysaver.domain.transaction.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val categoriesRepository: CategoriesRepository,
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    var state by mutableStateOf(CategoriesState())
        private set

    init {
        loadCategories()
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoriesRepository.insertCategory(category)
        }
    }
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.getTransactionsByCategoryUUID(category.uuid).onEach { list ->
                for(transaction in list) deleteTransaction(transaction)
            }.launchIn(viewModelScope)
            categoriesRepository.deleteCategory(category)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove spending data if transaction isn't new
            val transactionAccount = accountsRepository.getAccountByUUID(transaction.accountUUID)
            transactionAccount?.let {
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance-transaction.sum
                )
                // update account
                accountsRepository.insertAccount(updatedAccount)
            }

            repository.deleteTransaction(transaction)
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove previous spending data if transaction isn't new
            repository.getTransactionByUUID(transaction.uuid)?.let {
                val transactionAccount = accountsRepository.getAccountByUUID(it.accountUUID)
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance-it.sum
                )
                // update account
                accountsRepository.insertAccount(updatedAccount)
            }

            // add new spending data
            val transactionAccount = accountsRepository.getAccountByUUID(transaction.accountUUID)
            val updatedAccount = transactionAccount!!.copy(
                balance = transactionAccount.balance+transaction.sum
            )
            accountsRepository.insertAccount(updatedAccount)

            // add or update transaction
            repository.insertTransaction(transaction)
        }
    }

    fun loadCategories() {
        categoriesRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list as MutableList<Category>,
                )
            }.launchIn(viewModelScope)
    }

    fun loadCategoriesData() {

        repository.getTransactions().onEach { transactions ->

            val categories = state.categoriesList
            val tempList = mutableMapOf<Category, Double>()
            for(category in categories) {
                var categorySum = 0.0
                for(tr in transactions) {
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

        repository.getTransactionsInDateRange(minDate, maxDate).onEach { transactions ->

            val categories = state.categoriesList
            val tempList = mutableMapOf<Category, Double>()
            for(category in categories) {
                var categorySum = 0.0
                for(tr in transactions) {
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
        accountsRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    accountsList = list
                )
            }.launchIn(viewModelScope)
    }

}