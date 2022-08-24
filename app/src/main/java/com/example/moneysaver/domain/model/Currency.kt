package com.example.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Currency(
    @PrimaryKey val currencyName: String = "USD",
    val description: String = "",
    val currency: String = "$",
    val valueInMainCurrency:  Double = 0.0,
) : Serializable {

    override fun toString(): String {
        return "$currencyName,$description,$currency,$valueInMainCurrency"
    }
}