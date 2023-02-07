package com.andriyilliaandroidgeeks.moneysaver.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*
import kotlin.math.roundToInt

@Entity
data class Transaction(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
  //  @PrimaryKey val id: Int = 0,
    val sum: Double,
    val categoryUUID: UUID?,
    val accountUUID: UUID,
    val toAccountUUID: UUID?  = null,
    var toAccountSum: Double?  = null,
    val date: Date = Date(),
    val note: String? = null
) : Serializable {

    fun roundAllFields(){
        if (toAccountSum!=null)
            toAccountSum = (toAccountSum!! * 100.0).roundToInt() / 100.0
    }


    override fun toString(): String {
        return "\"$uuid\", \"$sum\", \"$categoryUUID\", \"$accountUUID\", \"$toAccountUUID\", \"$toAccountSum\", \"${date.time}\", \"$note\""
    }
}