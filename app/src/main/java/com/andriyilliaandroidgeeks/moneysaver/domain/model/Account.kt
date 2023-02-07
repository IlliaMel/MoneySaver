package com.andriyilliaandroidgeeks.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.VectorImg
import java.io.Serializable
import java.util.*
import kotlin.math.roundToInt


@Entity
data class Account(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val accountImg: VectorImg = AccountsData.accountImges[0],
    var currencyType: Currency = Currency(),
    val title: String,
    val description: String = "description",
    var balance: Double = 0.0,
    var creditLimit: Double = 0.0,
    var goal: Double = 0.0,
    var debt: Double = 0.0,
    val isForGoal: Boolean = false,
    val isForDebt: Boolean = false,
    val creationDate: Date = Date()
) : Serializable{

    fun roundAllFields(){
        balance = (balance * 100.0).roundToInt() / 100.0
        goal = (goal * 100.0).roundToInt() / 100.0
        debt = (debt * 100.0).roundToInt() / 100.0
        creditLimit = (creditLimit * 100.0).roundToInt() / 100.0
    }

    override fun toString(): String {
        return "\"$uuid\", \"$accountImg\", \"$currencyType\", \"$title\", \"$description\", \"$balance\", \"$creditLimit\", \"$goal\", \"$debt\", \"$isForGoal\", \"$isForDebt\", \"${creationDate.time}\""
    }
}
