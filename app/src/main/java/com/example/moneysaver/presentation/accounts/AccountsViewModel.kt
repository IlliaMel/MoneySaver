package com.example.moneysaver.presentation.accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.repository.FinanceRepository
import com.example.moneysaver.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    var state by mutableStateOf(AccountsState())
        private set

    init {
        loadAllAccounts()
        loadDebtAccounts()
        loadGoalAccounts()
        loadSimpleAccounts()
        loadSimpleAndDeptAccounts()

        loadCurrencyData()
    }

    private fun loadCurrencyData() {
        financeRepository.getCurrencyTypes()
            .onEach { list ->
                state = state.copy(
                    currenciesList = list,
                )
                AccountsData.currenciesList = list
            }.launchIn(viewModelScope)
    }

    fun returnCurrencyValue(which : String , to : String) : Double {
        var whichFound = state.currenciesList.find { it.currencyName == which }
        var toFound = state.currenciesList.find { it.currencyName == to }
        return (toFound?.valueInMainCurrency ?: 1.0) / (whichFound?.valueInMainCurrency ?: 1.0)
    }

    fun findSum(list: List<Account>, base : String) : Double{
        var sum = 0.0
        list.forEach {
            if(it.isForDebt){
                sum-=it.debt*returnCurrencyValue(it.currencyType.currencyName,base)
            }else if (it.isForGoal){
                sum+=it.balance*returnCurrencyValue(it.currencyType.currencyName,base)
            }else
                sum+=it.balance*returnCurrencyValue(it.currencyType.currencyName,base)
        }
        return Math.round(sum * 100) / 100.0
    }





    private fun loadDebtAccounts() {
        financeRepository.getDebtAccounts()
            .onEach { list ->
                state = state.copy(
                    debtList = list,
                )
            }.launchIn(viewModelScope)


    }
    private fun loadAllAccounts() {
        financeRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    allAccountList = list,
                )
            }.launchIn(viewModelScope)


    }


    fun loadGoalAccounts() {
        financeRepository.getGoalAccounts()
            .onEach { list ->
                state = state.copy(
                    goalList = list,
                )
            }.launchIn(viewModelScope)
    }

    fun loadSimpleAccounts() {
        financeRepository.getSimpleAccounts()
            .onEach { list ->
                state = state.copy(
                    simpleList = list,
                )
            }.launchIn(viewModelScope)
    }

    fun loadSimpleAndDeptAccounts() {
        financeRepository.getSimpleAndDeptAccounts()
            .onEach { list ->
                state = state.copy(
                    debtAndSimpleList = list,
                )
            }.launchIn(viewModelScope)
    }


    fun addAccount(account: Account) {
        viewModelScope.launch {
            financeRepository.insertAccount(account)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            financeRepository.getTransactionsByAccountUUID(account.uuid).onEach { list ->
                for(transaction in list) deleteTransaction(transaction)
            }.launchIn(viewModelScope)
            financeRepository.deleteAccount(account)
        }
        loadAllAccounts()
        loadDebtAccounts()
        loadGoalAccounts()
        loadSimpleAccounts()
        loadSimpleAndDeptAccounts()
    }

    fun loadBankAccountSum() : Double{
        var sum : Double = 0.0
        state.allAccountList.let{
            it.forEach(){
                if(!it.isForGoal){
                    if(it.isForDebt){
                        sum-=it.debt
                    }else{
                        sum+=it.balance
                    }
                }
            }
        }
        return sum
    }

    fun loadSavingsAccountSum() : Double{
        var sum : Double = 0.0
        state.goalList.let{
            it.forEach(){
                    sum+=it.balance
            }
        }
        return sum
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove spending data if transaction isn't new
            val transactionAccount = financeRepository.getAccountByUUID(transaction.accountUUID)
            transactionAccount?.let {
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance-transaction.sum
                )
                // update account
                financeRepository.insertAccount(updatedAccount)
            }

            financeRepository.deleteTransaction(transaction)
        }
    }

}