package com.andriyilliaandroidgeeks.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import kotlin.math.roundToInt

@Entity
data class Currency(
    @PrimaryKey var currencyName: String = "USD",
    var description: String = "",
    var currency: String = "$",
    var valueInMainCurrency:  Double = 0.0,
) : Serializable {

    fun roundAllFields(){
        valueInMainCurrency = (valueInMainCurrency * 100.0).roundToInt() / 100.0
    }

    override fun toString(): String {
        return "$currencyName,$description,$currency,$valueInMainCurrency"
    }
}