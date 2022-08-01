package com.example.moneysaver.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

     fun getTransactions(): Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun deleteAll()

}