package com.example.moneysaver.data.repository

import com.example.moneysaver.data.data_base.account_db.AccountDao
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow


class AccountsRepositoryImpl(
    private val dao: AccountDao
) : AccountsRepository {


    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAccounts()
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