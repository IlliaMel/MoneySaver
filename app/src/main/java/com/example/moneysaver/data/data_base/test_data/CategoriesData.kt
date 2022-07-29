package com.example.moneysaver.data.data_base.test_data

import com.example.moneysaver.R
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category

object CategoriesData {

    val categoriesList = listOf(
        Category(
            categoryImg = R.drawable.categories_icn,
            currencyType = "$",
            title = "Health",
            spended = 537.53,
        ),
        Category(
            categoryImg = R.drawable.categories_icn,
            currencyType = "$",
            title = "Entertainment",
            spended = 86.0,
        ),
        Category(
            categoryImg = R.drawable.categories_icn,
            currencyType = "$",
            title = "Gifts",
            spended = 0.0,
        ),
        Category(
            categoryImg = R.drawable.categories_icn,
            currencyType = "$",
            title = "Family",
            spended = 137.23,
        ),

        )
}