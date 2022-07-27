package com.example.moneysaver.presentation.accounts

import com.example.moneysaver.domain.account.Account

data class AccountsState (
    val accountList: ArrayList<Account>? = null,
    val currentAccount: Account
)