package com.example.moneysaver.presentation

import com.example.moneysaver.domain.model.Currency

data class MainActivityState(
    val currenciesList: List<Currency> = emptyList(),
)