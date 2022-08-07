package com.example.moneysaver.presentation.accounts

import com.example.moneysaver.domain.account.Account

data class AccountsState(
    val accountList: List<Account> = emptyList(),
    val goalList: List<Account> = emptyList(),
    val currentAccount: Account = Account(title = "Default")
)