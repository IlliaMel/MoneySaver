package com.example.moneysaver.data.data_base.test_data


import android.R.color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.moneysaver.R
import com.example.moneysaver.domain.account.Account
import java.io.Serializable


data class AccountImg(
    val img: Int = R.drawable.ic_account_img_1,
    val innerColor: Color =  Color(255, 255, 255, 255),
    val externalColor: Color =  Color(105, 105, 105, 255)
) : Serializable  {

    override fun toString(): String {
        return "$img,${innerColor.toArgb()},${externalColor.toArgb()}"
    }
}

data class CurrencyType(
    val description: String = "USA",
    val currency: String = "$"
)

object AccountsData {


    val accountImges = mutableListOf(
        AccountImg(img = R.drawable.ic_account_img_1, externalColor = Color(244, 67, 54, 255)),
        AccountImg(img = R.drawable.ic_account_img_2, externalColor = Color(233, 30, 99, 255)),
        AccountImg(img = R.drawable.ic_account_img_3, externalColor = Color(156, 39, 176, 255)),
        AccountImg(img = R.drawable.ic_account_img_4, externalColor = Color(103, 58, 183, 255)),
        AccountImg(img = R.drawable.ic_account_img_5, externalColor = Color(63, 81, 181, 255)),
        AccountImg(img = R.drawable.ic_account_img_6, externalColor = Color(33, 150, 243, 255)),
        AccountImg(img = R.drawable.ic_account_img_7, externalColor = Color(0, 150, 136, 255)),
        AccountImg(img = R.drawable.ic_account_img_8, externalColor = Color(76, 175, 80, 255)),
        AccountImg(img = R.drawable.ic_account_img_9, externalColor = Color(255, 235, 59, 255)),
        AccountImg(img = R.drawable.ic_account_img_10, externalColor = Color(255, 87, 34, 255)),
    )

    val currencyTypes = mutableListOf(
        CurrencyType(description = "European Euro" , currency = "€"),
        CurrencyType(description = "USA Dollar" , currency = "$"),
        CurrencyType(description = "Ukrainian Hryvnia" , currency = "₴"),
        CurrencyType(description = "Pound" , currency = "£"),
        CurrencyType(description = "Yuan" , currency = "¥"),
    )



    val deptAccount = Account(
        accountImg = AccountImg(),
        currencyType = "$",
        title = "Car Dept",
        description = "Dept for the car",
        debt = 0.0,
        isForDebt = true
    )

    val normalAccount = Account(
        accountImg = AccountImg(),
        currencyType = "$",
        title = "Cash",
        description = "Salary",
        balance = 0.0,
        creditLimit = 0.0,
    )

    val goalAccount = Account(
        accountImg = AccountImg(),
        currencyType = "$",
        title = "Stash",
        description = "For trip",
        balance = 0.0,
        creditLimit = 0.0,
        goal = 0.0,
        isForGoal = true
    )


    val accountAdder = Account(
    accountImg = AccountImg(),
    title = "Add Bank Account",
    )

    val goalAdder = Account(
        accountImg = AccountImg(),
        title = "Add Goal",
    )
    val accountsList = mutableListOf(
        Account(
            accountImg = AccountImg(),
            currencyType = "$",
            title = "Cash",
            balance = 537.53,
        ),
        Account(
            accountImg =AccountImg(),
            currencyType = "$",
            title = "Credit Card #1",
            balance = 15267.43,
        ),
        Account(
            accountImg = AccountImg(),
            currencyType = "$",
            title = "Credit Card #2",
            balance = 1678.63,
        ),

    )
}
