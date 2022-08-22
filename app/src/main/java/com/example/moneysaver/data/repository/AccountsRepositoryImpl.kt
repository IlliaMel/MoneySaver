package com.example.moneysaver.data.repository

import androidx.room.Query
import com.example.moneysaver.data.data_base.account_db.AccountDao
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import java.util.*


class AccountsRepositoryImpl(
    private val dao: AccountDao
) : AccountsRepository {

    override suspend fun getAccountByUUID(uuid: UUID): Account? {
        return dao.getAccountByUUID(uuid)
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return dao.getAllAccounts()
    }

    override fun getSimpleAndDeptAccounts(): Flow<List<Account>> {
        return dao.getSimpleAndDeptAccounts()
    }
    override fun getSimpleAccounts(): Flow<List<Account>> {
        return dao.getSimpleAccounts()
    }

    override fun getDebtAccounts(): Flow<List<Account>> {
        return dao.getDebtAccounts()
    }

    override fun getGoalAccounts(): Flow<List<Account>> {
        return dao.getGoalAccounts()
    }

    override suspend fun insertAccount(account: Account) {
        return dao.insertAccount(account)
    }

    override suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }

    override suspend fun deleteAllAccounts() {
        dao.deleteAll()
    }

}