package com.example.moneysaver.presentation.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moneysaver.data.data_base.test_data.TransactionsData
import com.example.moneysaver.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionRepository,
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

    fun deleteTransactions() {
        viewModelScope.launch {
            repository.deleteAllTransaction()
        }
    }


}