package com.example.moneysaver.data.data_base

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.TypeConverter
import com.example.moneysaver.data.data_base.test_data.AccountImg
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import java.lang.Float.valueOf
import java.util.*

class Converters {



    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromStringToCategory(value: String?): Category? {
        value?.let {
            val strArr = it.split(",")
            return Category(categoryImg = strArr[0].toInt(),currencyType = strArr[1],title = strArr[2],spent = strArr[3].toDouble())
        }
        return Category(title = "")
    }


    @TypeConverter
    fun stringToCategory(category: Category?): String? {
        return category.toString()
    }
/*
    @TypeConverter
    fun fromStringToAccount(value: String?): Account? {
        value?.let {
            val strArr = it.split(",")
            return Account(accountImg = AccountImg( strArr[0].toInt(),Color(strArr[1].toInt(),strArr[2].toInt(),strArr[3].toInt(),strArr[4].toInt()),Color(strArr[5].toInt(),strArr[6].toInt(),strArr[7].toInt(),strArr[8].toInt())),
                currencyType = strArr[9],
                title = strArr[10],
                description = strArr[11],
                balance = strArr[12].toDouble(),
                creditLimit = strArr[13].toDouble(),
                goal = strArr[14].toDouble(),
                debt = strArr[15].toDouble(),
                isForGoal = strArr[16].toBoolean(),
                isForDebt = strArr[17].toBoolean(),
            )
        }
        return Account(title = "")
    }
    */

    @TypeConverter
    fun fromStringToAccount(value: String?): Account? {
        value?.let {
            val strArr = it.split(",")
            return Account(accountImg = AccountImg( strArr[0].toInt(),Color(strArr[1].toInt()),Color(strArr[2].toInt())),
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

    @TypeConverter
    fun stringToAccount(account: Account?): String? {
        return account.toString()
    }

    @TypeConverter
    fun fromStringToAccountImg(value: String?): AccountImg? {
        value?.let {
            val strArr = it.split(",")
            return  AccountImg( strArr[0].toInt(),Color(strArr[1].toInt()),Color(strArr[2].toInt()))
        }
        return AccountImg()
    }

    @TypeConverter
    fun stringToAccountImg(accountImg: AccountImg?): String? {
        return accountImg.toString()
    }

}