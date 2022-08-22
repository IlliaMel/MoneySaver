package com.example.moneysaver.data.repository

import com.example.moneysaver.data.data_base.currency_db.CurrencyDao
import com.example.moneysaver.data.remote.CurrencyApi
import com.example.moneysaver.domain.currency.Currency

import com.example.moneysaver.domain.repository.CurrencyApiRepository
import com.example.moneysaver.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val dao: CurrencyDao
): CurrencyRepository {

    override fun getCurrencyTypes(): Flow<List<Currency>> {
        return dao.getCurrencyTypes()
    }

    override suspend fun insertCurrencyType(currencyType: Currency){
        dao.insertCurrencyType(currencyType)
    }

    override suspend fun deleteAll(){
        dao.deleteAll()
    }
}