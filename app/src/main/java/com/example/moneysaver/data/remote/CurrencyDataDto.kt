package com.example.moneysaver.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//EUR,USD,UAH,GBP,CNY,AED,TRY,BTC
data class Rates(
    @field:Json(name = "EUR")
    val eUR: Double,
    @field:Json(name = "USD")
    val uSD: Double,
    @field:Json(name = "UAH")
    val uAH: Double,
    @field:Json(name = "GBP")
    val gBP: Double,
    @field:Json(name = "CNY")
    val cNY: Double,
    @field:Json(name = "AED")
    val aED: Double,
    @field:Json(name = "TRY")
    val tRY: Double,
    @field:Json(name = "BTC")
    val bTC: Double,
)

