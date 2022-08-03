package com.example.moneysaver.data.data_base

import androidx.room.TypeConverter
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
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


    @TypeConverter
    fun fromStringToAccount(value: String?): Account? {
        value?.let {
            val strArr = it.split(",")
            return Account(accountImg = strArr[0].toInt(),
                currencyType = strArr[1],
                title = strArr[2],
                balance = strArr[3].toDouble(),
                debt = strArr[4].toDouble(),
                goal = strArr[5].toDouble(),
                isForGoal = strArr[6].toBoolean(),
                isForDebt = strArr[7].toBoolean(),
            )
        }
        return Account(title = "")
    }

    @TypeConverter
    fun stringToAccount(account: Account?): String? {
        return account.toString()
    }


}