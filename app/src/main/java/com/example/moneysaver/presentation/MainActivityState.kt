package com.example.moneysaver.presentation

import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.model.Transaction

data class MainActivityState(
    val currenciesList: List<Currency> = emptyList(),

    // CategoriesState
    val categoriesList: MutableList<Category> = ArrayList(),
    // val currenciesList: List<Currency> = emptyList(),
    val categoriesSums: MutableMap<Category, Double> = mutableMapOf<Category, Double>(), // <category - sum of all category transactions>
    val accountsList: List<Account> = emptyList(),
    val selectedAccount: Account? = null,

    //Transactions
    val transactionList: List<Transaction> = emptyList(),
    val startingBalance: Double = 0.0,
    val endingBalance: Double = 0.0,
    //val categoriesList: List<Category> = emptyList(),
    //val accountsList: List<Account> = emptyList()

    val isDataLoading: Boolean = false


)