package com.example.moneysaver.data.data_base.test_data


import com.example.moneysaver.R
import com.example.moneysaver.domain.account.Account
import java.util.*


data class CurrencyType(
    val description: String = "USA",
    val currency: String = "$"
)

object AccountsData {

    val currencyTypes = mutableListOf(
        CurrencyType(description = "European Euro" , currency = "€"),
        CurrencyType(description = "USA Dollar" , currency = "$"),
        CurrencyType(description = "Ukrainian Hryvnia" , currency = "₴"),
        CurrencyType(description = "Pound" , currency = "£"),
        CurrencyType(description = "Yuan" , currency = "¥"),
    )



    val deptAccount = Account(
        accountImg = R.drawable.cash,
        currencyType = "$",
        title = "Car Dept",
        description = "Dept for the car",
        debt = 0.0,
        isForDebt = true
    )

    val normalAccount = Account(
        accountImg = R.drawable.cash,
        currencyType = "$",
        title = "Cash",
        description = "Salary",
        balance = 0.0,
        creditLimit = 0.0,
    )

    val goalAccount = Account(
        accountImg = R.drawable.cash,
        currencyType = "$",
        title = "Stash",
        description = "For trip",
        balance = 0.0,
        creditLimit = 0.0,
        goal = 0.0,
        isForGoal = true
    )

    /*

        val accountImg: Int = R.drawable.cash,
    val currencyType: String = "$",
    val title: String,
    val description: String = "",
    val balance: Double = 0.0,
    val creditLimit: Double = 0.0,
    val goal: Double = 0.0,
    val isForGoal: Boolean = false,
    val isForDebt: Boolean = false,
     */

    val accountAdder = Account(
    accountImg = R.drawable.add_card,
    title = "Add Bank Account",
    )

    val goalAdder = Account(
        accountImg = R.drawable.add_card,
        title = "Add Goal",
    )
    val accountsList = mutableListOf(
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
