package com.example.moneysaver.presentation.transactions

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moneysaver.data.data_base.test_data.TransactionsData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class TransactionsViewModel():ViewModel() {

    var state by mutableStateOf(TransactionsState())
        private set

    fun loadTransactions() {
        state = state.copy(
            transactionList = TransactionsData.transactionList,
            startingBalance = 0.0,
            endingBalance = 23450.5
        ) }

}