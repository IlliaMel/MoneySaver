package com.example.moneysaver.presentation.transactions


import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction

data class TransactionsState (
    val transactionList: List<Transaction> = emptyList(),
    val startingBalance: Double = 0.0,
    val endingBalance: Double = 0.0,
    val categoriesList: List<Category> = emptyList(),
)