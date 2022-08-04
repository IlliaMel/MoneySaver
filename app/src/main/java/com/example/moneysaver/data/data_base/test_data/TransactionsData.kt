package com.example.moneysaver.data.data_base.test_data

import com.example.moneysaver.domain.transaction.Transaction
import java.util.*

object TransactionsData {
    val transactionList = listOf(
        Transaction(
            sum = -250.5,
            category = CategoriesData.categoriesList.get(0),
            account = AccountsData.accountsList.get(0),
            date = Date(2021, 6, 5),
            note = "Bus"
        ),
        Transaction(
            sum = 22000.0,
            category = CategoriesData.categoriesList.get(1),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 4, 22)
        ),
        Transaction(
            sum = -1205.5,
            category = CategoriesData.categoriesList.get(2),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 6, 9),
            note = "Some text"
        ),
        Transaction(
            sum = -1205.5,
            category = CategoriesData.categoriesList.get(3),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 6, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = -1205.5,
            category = CategoriesData.categoriesList.get(4),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 6, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = -1205.5,
            category = CategoriesData.categoriesList.get(5),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 6, 7),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = -1205.5,
            category = CategoriesData.categoriesList.get(6),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 6, 8),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = -1205.5,
            category = CategoriesData.categoriesList.get(4),
            account = AccountsData.accountsList.get(0),
            date = Date(2022, 6, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        )

    )
}