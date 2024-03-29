package com.andriyilliaandroidgeeks.moneysaver.data.data_base

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.VectorImg
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import java.util.*

import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency

class Converters {



    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
/*
    @TypeConverter
    fun fromStringToCategory(value: String?): Category? {
        value?.let {
            val strArr = it.split(",")
            return Category(categoryImg = VectorImg( strArr[0].toInt(),Color(strArr[1].toInt()),Color(strArr[2].toInt())),currencyType = strArr[3],title = strArr[4],spent = strArr[5].toDouble())
        }
        return Category(title = "")
    }
*/

    @TypeConverter
    fun stringToCategory(category: Category?): String? {
        return category.toString()
    }

    /*
    @TypeConverter
    fun fromStringToAccount(value: String?): Account? {
        value?.let {
            val strArr = it.split(",")
            return Account(accountImg = VectorImg( strArr[0].toInt(),Color(strArr[1].toInt()),Color(strArr[2].toInt())),
                currencyType = strArr[3],
                title = strArr[4],
                description = strArr[5],
                balance = strArr[6].toDouble(),
                creditLimit = strArr[7].toDouble(),
                goal = strArr[8].toDouble(),
                debt = strArr[9].toDouble(),
                isForGoal = strArr[10].toBoolean(),
                isForDebt = strArr[11].toBoolean(),
            )
        }
        return Account(title = "")
    }
    */

    @TypeConverter
    fun stringToAccount(account: Account?): String? {
        return account.toString()
    }

    @TypeConverter
    fun fromStringToVectorImg(value: String?): VectorImg? {
        value?.let {
            val strArr = it.split(",")
            return  VectorImg( strArr[0].toInt(),Color(strArr[1].toInt()),Color(strArr[2].toInt()))
        }
        return VectorImg()
    }

    @TypeConverter
    fun stringToVectorImg(accountImg: VectorImg?): String? {
        return accountImg.toString()
    }

    @TypeConverter
    fun stringToCurrency(currency: Currency?): String? {
        return currency.toString()
    }

    @TypeConverter
    fun fromStringToCurrency(value: String?): Currency? {
        value?.let {
            val strArr = it.split(",")
            return  Currency( currencyName = strArr[0],description = strArr[1],currency = strArr[2],valueInMainCurrency = strArr[3].toDouble())
        }
        return Currency()
    }




}