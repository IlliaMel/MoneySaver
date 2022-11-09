package com.andriyilliaandroidgeeks.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Currency(
    @PrimaryKey var currencyName: String = "USD",
    var description: String = "",
    var currency: String = "$",
    var valueInMainCurrency:  Double = 0.0,
) : Serializable {

    override fun toString(): String {
        return "$currencyName,$description,$currency,$valueInMainCurrency"
    }
}