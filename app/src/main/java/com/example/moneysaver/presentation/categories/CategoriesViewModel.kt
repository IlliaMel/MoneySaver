package com.example.moneysaver.presentation.categories

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.repository.TransactionRepository
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.presentation.transactions.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: TransactionRepository,
) : ViewModel() {

    var state by mutableStateOf(CategoriesState())
        private set

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun loadCategoriesData() {

        repository.getTransactions().onEach { transactions ->

            val categories = CategoriesData.categoriesList
            val tempList = mutableMapOf<Category, Double>()
            for(category in categories) {
                var categorySum = 0.0
                for(tr in transactions) {
                    /*TODO: compare with uuid instead of title when categories db will be filled */
                    if(tr.category.title == category.title) {
                        categorySum+=tr.sum
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

    fun loadCategoriesDataInDateRange(minDate: Date, maxDate: Date) {

        repository.getTransactionsInDateRange(minDate, maxDate).onEach { transactions ->

            val categories = CategoriesData.categoriesList
            val tempList = mutableMapOf<Category, Double>()
            for(category in categories) {
                var categorySum = 0.0
                for(tr in transactions) {
                    /*TODO: compare with uuid instead of title when categories db will be filled */
                    if(tr.category.title == category.title) {
                        categorySum+=tr.sum
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

}