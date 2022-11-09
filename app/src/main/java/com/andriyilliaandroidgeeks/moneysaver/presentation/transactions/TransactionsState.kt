package com.andriyilliaandroidgeeks.moneysaver.presentation.transactions


import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction

data class TransactionsState (
    val transactionList: List<Transaction> = emptyList(),
    val startingBalance: Double = 0.0,
    val endingBalance: Double = 0.0,
    val categoriesList: List<Category> = emptyList(),
    val accountsList: List<Account> = emptyList()
)