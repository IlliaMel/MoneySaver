package com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivity
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.externalColorGray
import java.io.Serializable


data class VectorImg(
    var img: Int = R.drawable.ic_account_img_0,
    var innerColor: Color =  Color(255, 255, 255, 255),
    var externalColor: Color =  externalColorGray
) : Serializable  {

    override fun toString(): String {
        return "$img,${innerColor.toArgb()},${externalColor.toArgb()}"
    }
}



object AccountsData {

    var accountBgImg = R.drawable.bg6
    var cardBgImg = R.drawable.bg5

    val accountImges = mutableListOf(
        VectorImg(img = R.drawable.ic_account_img_1),
        VectorImg(img = R.drawable.ic_account_img_2),
        VectorImg(img = R.drawable.ic_account_img_3),
        VectorImg(img = R.drawable.ic_account_img_4),
        VectorImg(img = R.drawable.ic_account_img_5),
        VectorImg(img = R.drawable.ic_account_img_6),
        VectorImg(img = R.drawable.ic_account_img_7),
        VectorImg(img = R.drawable.ic_account_img_8),
        VectorImg(img = R.drawable.ic_account_img_9),
        VectorImg(img = R.drawable.ic_account_img_9_0),
        VectorImg(img = R.drawable.ic_account_img_9_1),
        VectorImg(img = R.drawable.ic_account_img_9_2),
        VectorImg(img = R.drawable.ic_account_img_9_3),
        VectorImg(img = R.drawable.ic_account_img_9_4),
        VectorImg(img = R.drawable.ic_account_img_9_5),
    )


    var mainCurrency = Currency(description = "European Euro" , currency = "€", currencyName = "EUR", valueInMainCurrency = 1.0)

    var currenciesList: List<Currency> = emptyList()

    val currencyTypes = mutableListOf(
        Currency(description = "European Euro" , currency = "€", currencyName = "EUR"), // EUR
        Currency(description = "USA Dollar" , currency = "$", currencyName = "USD"), //USD
        Currency(description = "Ukrainian Hryvnia" , currency = "₴", currencyName = "UAH"), // UAH
        Currency(description = "Pound" , currency = "£", currencyName = "GBP"), //GBP
        Currency(description = "Yuan" , currency = "¥", currencyName = "CNY"), //CNY
        Currency(description = "UAE Dirham" , currency = "AED", currencyName = "AED"), //AED
        Currency(description = "Turkish Lira" , currency = "¥", currencyName = "TRY"), //TRY
        Currency(description = "Bitcoin" , currency = "BTC", currencyName = "BTC"), //BTC
        )


    var deptAccount = Account(
        accountImg = VectorImg(),
        currencyType = Currency(),
        title = MainActivity.instance!!.getString(R.string.car_dept),
        description = MainActivity.instance!!.getString(R.string.dept_for_the_car),
        debt = 0.0,
        isForDebt = true
    )

    var normalAccount = Account(
        accountImg = VectorImg(),
        currencyType = Currency(),
        title = MainActivity.instance!!.getString(R.string.cash),
        description = MainActivity.instance!!.getString(R.string.salary),
        balance = 0.0,
        creditLimit = 0.0,
    )

    var goalAccount = Account(
        accountImg = VectorImg(),
        currencyType = Currency(),
        title = MainActivity.instance!!.getString(R.string.stash),
        description = MainActivity.instance!!.getString(R.string.for_trip),
        balance = 0.0,
        creditLimit = 0.0,
        goal = 0.0,
        isForGoal = true
    )

    var allAccountFilter = Account(
        accountImg = VectorImg(img = R.drawable.ic_account_img_3),
        currencyType = Currency(),
        title = MainActivity.instance!!.getString(R.string.all_accounts),
        description = "",
        balance = 0.0,
        creditLimit = 0.0,
        goal = 0.0,
        isForGoal = true,
        isForDebt = true
    )

    var accountAdder = Account(
    accountImg = VectorImg(),
    title = MainActivity.instance!!.getString(R.string.add_bank_account),
    )

    var goalAdder = Account(
        accountImg = VectorImg(),
        title = MainActivity.instance!!.getString(R.string.add_goal),
    )
    var accountsList = mutableListOf(
        Account(
            accountImg = VectorImg(),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.cash),
            balance = 0.0,
        )
    )

    fun update() {
        deptAccount = Account(
            accountImg = VectorImg(),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.car_dept),
            description = MainActivity.instance!!.getString(R.string.dept_for_the_car),
            debt = 0.0,
            isForDebt = true
        )
        normalAccount = Account(
            accountImg = VectorImg(),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.cash),
            description = MainActivity.instance!!.getString(R.string.salary),
            balance = 0.0,
            creditLimit = 0.0,
        )
        goalAccount = Account(
            accountImg = VectorImg(),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.stash),
            description = MainActivity.instance!!.getString(R.string.for_trip),
            balance = 0.0,
            creditLimit = 0.0,
            goal = 0.0,
            isForGoal = true
        )
        allAccountFilter = Account(
            accountImg = VectorImg(),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.all_accounts),
            description = "",
            balance = 0.0,
            creditLimit = 0.0,
            goal = 0.0,
            isForGoal = true,
            isForDebt = true
        )
        accountAdder = Account(
            accountImg = VectorImg(),
            title = MainActivity.instance!!.getString(R.string.add_bank_account),
        )
        goalAdder = Account(
            accountImg = VectorImg(),
            title = MainActivity.instance!!.getString(R.string.add_goal),
        )
        accountsList = mutableListOf(
            Account(
                accountImg = VectorImg(),
                currencyType = Currency(),
                title = MainActivity.instance!!.getString(R.string.cash),
                balance = 0.0,
            ),
            Account(
                accountImg =VectorImg(),
                currencyType = Currency(),
                title = "Credit Card #1",
                balance = 15267.43,
            ),
            Account(
                accountImg = VectorImg(),
                currencyType = Currency(),
                title = "Credit Card #2",
                balance = 1678.63,
            ),

            )
    }
}
