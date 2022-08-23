package com.example.moneysaver.data.repository

import androidx.room.Query
import com.example.moneysaver.data.data_base.transaction_dp.TransactionDao
import com.example.moneysaver.domain.repository.TransactionRepository
import com.example.moneysaver.domain.transaction.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {

    override suspend fun getTransactionByUUID(uuid: UUID): Transaction? {
        return dao.getTransactionByUUID(uuid)
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions()
    }

    override fun getTransactionsByCategoryUUID(categoryUUID: UUID): Flow<List<Transaction>> {
        return dao.getTransactionsByCategoryUUID(categoryUUID)
    }

    override fun getTransactionsInDateRange(minDate: Date, maxDate: Date): Flow<List<Transaction>> {
        return dao.getTransactionsInDateRange(minDate, maxDate)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        return dao.insertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }

    override suspend fun deleteAllTransaction() {
        dao.deleteAll()
    }

}

