package com.example.moneysaver.domain.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.test_data.AccountImg
import com.example.moneysaver.data.data_base.test_data.AccountsData
import java.io.Serializable
import java.util.*

@Entity
data class Account(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val accountImg: AccountImg = AccountsData.accountImges[0],
    val currencyType: String = "$",
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
