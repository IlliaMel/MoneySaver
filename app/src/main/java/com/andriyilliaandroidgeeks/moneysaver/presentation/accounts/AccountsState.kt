package com.andriyilliaandroidgeeks.moneysaver.presentation.accounts

import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency

data class AccountsState(
    val allAccountList: List<Account> = emptyList(),
    val debtAndSimpleList: List<Account> = emptyList(),
    val debtList: List<Account> = emptyList(),
    val simpleList: List<Account> = emptyList(),
    val goalList: List<Account> = emptyList(),
    val currenciesList: List<Currency> = emptyList(),
    val currentAccount: Account = Account(title = "Default")
)