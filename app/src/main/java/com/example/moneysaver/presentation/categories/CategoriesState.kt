package com.example.moneysaver.presentation.categories

import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category

data class CategoriesState(
    val categoriesList: MutableList<Category> = ArrayList(),
    val categoriesSums: MutableMap<Category, Double> = mutableMapOf<Category, Double>(), // <category - sum of all category transactions>
    val accountsList: List<Account> = emptyList()
)