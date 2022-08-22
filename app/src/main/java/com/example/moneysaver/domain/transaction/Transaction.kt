package com.example.moneysaver.domain.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Transaction(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
  //  @PrimaryKey val id: Int = 0,
    val sum: Double,
    val categoryUUID: UUID,
    val accountUUID: UUID,
    val date: Date = Date(),
    val note: String? = null
)