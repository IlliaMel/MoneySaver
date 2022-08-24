package com.example.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.VectorImg
import java.io.Serializable
import java.util.*

@Entity
data class Category(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val categoryImg: VectorImg = AccountsData.accountImges[0],
    val currencyType: String = "$",
    val title: String,
    val spent: Double = 0.0,
) : Serializable{
    
    override fun toString(): String {
        return "$categoryImg,$currencyType,$title,$spent"
    }
}
