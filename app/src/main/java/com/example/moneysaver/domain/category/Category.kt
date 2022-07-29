package com.example.moneysaver.domain.category

import java.io.Serializable
import java.util.*

data class Category(
    val categoryImg: Int = 0,
    val currencyType: String = "$",
    val title: String,
    val spended: Double = 0.0,
) : Serializable
