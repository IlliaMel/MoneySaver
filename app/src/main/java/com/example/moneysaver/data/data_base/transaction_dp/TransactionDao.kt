package com.example.moneysaver.data.data_base.transaction_dp

import androidx.room.*
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction` WHERE uuid = :uuid")
    suspend fun getTransactionByUUID(uuid: UUID): Transaction?

    @Query("SELECT * FROM `transaction`")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction`  WHERE  categoryUUID = :categoryUUID")
    fun getTransactionsByCategoryUUID(categoryUUID: UUID): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction`  WHERE  accountUUID = :accountUUID")
    fun getTransactionsByAccountUUID(accountUUID: UUID): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :minDate AND :maxDate")
    fun getTransactionsInDateRange(minDate: Date, maxDate: Date): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAll()

}
