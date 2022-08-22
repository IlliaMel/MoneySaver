package com.example.moneysaver.presentation

import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.currency.Currency

data class MainActivityState(
    val currenciesList: List<Currency> = emptyList(),
)