package com.example.moneysaver.data.data_base.test_data

import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.presentation.transactions.Transactions
import java.util.*

object TransactionsData {
    val transactionList = listOf(
        Transaction(
            sum = 250.5,
            isIncome = false,
            category = "Transport",
            account = "Card",
            date = Date(2022, 7, 5),
            note = "Bus"
        ),
        Transaction(
            sum = 22000.0,
            isIncome = true,
            category = "Salary",
            account = "Card",
            date = Date(2022, 7, 22)
        ),
        Transaction(
            sum = 1205.5,
            isIncome = false,
            category = "Restaurant",
            account = "Card",
            date = Date(2022, 7, 9),
            note = "Some text"
        ),
        Transaction(
            sum = 1205.5,
            isIncome = false,
            category = "Restaurant",
            account = "Card",
            date = Date(2022, 7, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = 1205.5,
            isIncome = false,
            category = "Restaurant",
            account = "Card",
            date = Date(2022, 7, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = 1205.5,
            isIncome = false,
            category = "Restaurant",
            account = "Card",
            date = Date(2022, 7, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = 1205.5,
            isIncome = false,
            category = "Restaurant",
            account = "Card",
            date = Date(2022, 7, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        ),
        Transaction(
            sum = 1205.5,
            isIncome = false,
            category = "Restaurant",
            account = "Card",
            date = Date(2022, 7, 9),
            note = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
        )

    )
}