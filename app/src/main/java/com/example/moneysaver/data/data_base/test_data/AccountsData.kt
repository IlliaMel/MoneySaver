package com.example.moneysaver.data.data_base.test_data


import android.R.color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.moneysaver.R
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.ui.theme.externalColorGray
import java.io.Serializable


data class VectorImg(
    var img: Int = R.drawable.ic_account_img_1,
    var innerColor: Color =  Color(255, 255, 255, 255),
    var externalColor: Color =  externalColorGray
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
        VectorImg(img = R.drawable.ic_account_img_1),
        VectorImg(img = R.drawable.ic_account_img_2),
        VectorImg(img = R.drawable.ic_account_img_3),
        VectorImg(img = R.drawable.ic_account_img_4),
        VectorImg(img = R.drawable.ic_account_img_5),
        VectorImg(img = R.drawable.ic_account_img_6),
        VectorImg(img = R.drawable.ic_account_img_7),
        VectorImg(img = R.drawable.ic_account_img_8),
        VectorImg(img = R.drawable.ic_account_img_9),
        VectorImg(img = R.drawable.ic_account_img_10),
    )

    val currencyTypes = mutableListOf(
        CurrencyType(description = "European Euro" , currency = "€"),
        CurrencyType(description = "USA Dollar" , currency = "$"),
        CurrencyType(description = "Ukrainian Hryvnia" , currency = "₴"),
        CurrencyType(description = "Pound" , currency = "£"),
        CurrencyType(description = "Yuan" , currency = "¥"),
    )



    val deptAccount = Account(
        accountImg = VectorImg(),
        currencyType = "$",
        title = "Car Dept",
        description = "Dept for the car",
        debt = 0.0,
        isForDebt = true
    )

    val normalAccount = Account(
        accountImg = VectorImg(),
        currencyType = "$",
        title = "Cash",
        description = "Salary",
        balance = 0.0,
        creditLimit = 0.0,
    )

    val goalAccount = Account(
        accountImg = VectorImg(),
        currencyType = "$",
        title = "Stash",
        description = "For trip",
        balance = 0.0,
        creditLimit = 0.0,
        goal = 0.0,
        isForGoal = true
    )


    val accountAdder = Account(
    accountImg = VectorImg(),
    title = "Add Bank Account",
    )

    val goalAdder = Account(
        accountImg = VectorImg(),
        title = "Add Goal",
    )
    val accountsList = mutableListOf(
        Account(
            accountImg = VectorImg(),
            currencyType = "$",
            title = "Cash",
            balance = 537.53,
        ),
        Account(
            accountImg =VectorImg(),
            currencyType = "$",
            title = "Credit Card #1",
            balance = 15267.43,
        ),
        Account(
            accountImg = VectorImg(),
            currencyType = "$",
            title = "Credit Card #2",
            balance = 1678.63,
        ),

    )
}
