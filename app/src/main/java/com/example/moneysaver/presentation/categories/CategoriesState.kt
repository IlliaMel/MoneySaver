package com.example.moneysaver.presentation.categories

import com.example.moneysaver.domain.category.Category

data class CategoriesState (
    val categoriesList: List<Category> = emptyList(),
    val categoriesSums: MutableMap<Category, Double> = mutableMapOf<Category, Double>() // <category - sum of all category transactions>
)