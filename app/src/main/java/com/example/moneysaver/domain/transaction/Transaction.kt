package com.example.moneysaver.domain.transaction

import java.util.*

data class Transaction(
    val sum: Double,
    val isIncome: Boolean,
    val category: String,
    val account: String,
    val date: Date,
    val note: String? = null
)