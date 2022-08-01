package com.example.moneysaver.presentation.transactions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moneysaver.data.data_base.TransactionDao
import com.example.moneysaver.data.data_base.test_data.TransactionsData
import com.example.moneysaver.data.repository.TransactionRepositoryImpl
import com.example.moneysaver.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionRepository,
) : ViewModel() {

    var state by mutableStateOf(TransactionsState())
        private set


    fun loadTransactions() {
        viewModelScope.launch {
            TransactionsData.transactionList.forEach() {
                repository.insertTransaction(it)
           }
        }
    }

    fun getTransactions() {
            repository.getTransactions()
                .onEach { list ->
                    state = state.copy(
                        transactionList = list
                    )
                }.launchIn(viewModelScope)


    }

    fun deleteTransactions() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }


}