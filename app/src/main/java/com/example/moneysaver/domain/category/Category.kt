package com.example.moneysaver.domain.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Category(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    val categoryImg: Int = 0,
    val currencyType: String = "$",
    val title: String,
    val spent: Double = 0.0,
) : Serializable{
    
    override fun toString(): String {
        return "$categoryImg,$currencyType,$title,$spent"
    }
}
