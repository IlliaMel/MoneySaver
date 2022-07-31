package com.example.moneysaver.domain.account

import java.io.Serializable
import java.util.*

data class Account(
    val accountImg: Int = 0,
    val currencyType: String = "$",
    val title: String,
    val balance: Double = 0.0,
    val debt: Double = 0.0,
    val goal: Double = 0.0,
    val isForGoal: Boolean = false,
    val isForDebt: Boolean = false,
) : Serializable{

    override fun toString(): String {
        return "$accountImg,$currencyType,$title,$balance,$balance,$debt,$goal,$isForGoal,$isForDebt"
    }
}
