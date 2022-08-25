package com.example.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.VectorImg
import java.io.Serializable
import java.util.*


@Entity
data class Account(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val accountImg: VectorImg = AccountsData.accountImges[0],
    val currencyType: Currency = Currency(),
    val title: String,
    val description: String = "description",
    val balance: Double = 0.0,
    val creditLimit: Double = 0.0,
    val goal: Double = 0.0,
    val debt: Double = 0.0,
    val isForGoal: Boolean = false,
    val isForDebt: Boolean = false,
) : Serializable{

    override fun toString(): String {
        return "$accountImg,$currencyType,$title,$description,$balance,$creditLimit,$goal,$debt,$isForGoal,$isForDebt"
    }
}