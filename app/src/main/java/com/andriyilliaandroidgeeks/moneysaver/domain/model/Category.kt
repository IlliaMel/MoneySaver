package com.andriyilliaandroidgeeks.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.VectorImg
import java.io.Serializable
import java.util.*

@Entity
data class Category(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val categoryImg: VectorImg = AccountsData.accountImges[0],
    var currencyType: Currency = Currency(),
    val title: String,
    val isForSpendings: Boolean = true,
    var spent: Double = 0.0,
    val creationDate: Date = Date()
) : Serializable{
    
    override fun toString(): String {
        return "\"$uuid\", \"$categoryImg\", \"$currencyType\", \"$title\", \"$isForSpendings\", \"$spent\", \"${creationDate.time}\""
    }
}
