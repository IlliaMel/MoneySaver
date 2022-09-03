package com.example.moneysaver.presentation.categories

import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency

data class CategoriesState(
    val categoriesList: List<Category> = ArrayList(),
    val currenciesList: List<Currency> = emptyList(),
    val categoriesSums: MutableMap<Category, Double> = mutableMapOf<Category, Double>(), // <category - sum of all category transactions>
    val accountsList: List<Account> = emptyList(),
    val selectedAccount: Account? = null,
    var isForSpendings: Boolean = true,
    val spend: Double = 0.0,
    val earned: Double = 0.0
)