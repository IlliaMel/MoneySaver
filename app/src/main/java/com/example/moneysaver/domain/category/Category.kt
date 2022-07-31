package com.example.moneysaver.domain.category

import java.io.Serializable
import java.util.*

data class Category(
    val categoryImg: Int = 0,
    val currencyType: String = "$",
    val title: String,
    val spent: Double = 0.0,
) : Serializable{
    
    override fun toString(): String {
        return "$categoryImg,$currencyType,$title,$spent"
    }
}
