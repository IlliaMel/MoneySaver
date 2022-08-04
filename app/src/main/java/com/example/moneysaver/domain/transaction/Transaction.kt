package com.example.moneysaver.domain.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import java.util.*

@Entity
data class Transaction(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
  //  @PrimaryKey val id: Int = 0,
    val sum: Double,
    val category: Category,
    val account: Account,
    val date: Date,
    val note: String? = null
)