package com.example.moneysaver.data.data_base.test_data


import com.example.moneysaver.R
import com.example.moneysaver.domain.account.Account
import java.util.*

object AccountsData {

    val accountAdder = Account(
    accountImg = R.drawable.add_card,
    title = "Add Bank Account",
    )

    val goalAdder = Account(
        accountImg = R.drawable.add_card,
        title = "Add Goal",
    )
    val accountsList = listOf(
        Account(
            accountImg = R.drawable.cash,
            currencyType = "$",
            title = "Cash",
            balance = 537.53,
        ),
        Account(
            accountImg = R.drawable.credit_card,
            currencyType = "$",
            title = "Credit Card #1",
            balance = 15267.43,
        ),
        Account(
            accountImg = R.drawable.credit_card,
            currencyType = "$",
            title = "Credit Card #2",
            balance = 1678.63,
        ),

    )
}
