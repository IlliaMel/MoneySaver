package com.example.moneysaver.presentation.accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.repository.AccountsRepository
import com.example.moneysaver.domain.repository.TransactionRepository
import com.example.moneysaver.domain.transaction.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val repository: AccountsRepository,
) : ViewModel() {

    init {
        loadAllAccounts()
        loadDebtAccounts()
        loadGoalAccounts()
        loadSimpleAccounts()
        loadSimpleAndDeptAccounts()
    }


    var state by mutableStateOf(AccountsState())
        private set


    private fun loadDebtAccounts() {
        repository.getDebtAccounts()
            .onEach { list ->
                state = state.copy(
                    debtList = list,
                )
            }.launchIn(viewModelScope)


    }
    private fun loadAllAccounts() {
        repository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    allAccountList = list,
                )
            }.launchIn(viewModelScope)


    }


    fun loadGoalAccounts() {
        repository.getGoalAccounts()
            .onEach { list ->
                state = state.copy(
                    goalList = list,
                )
            }.launchIn(viewModelScope)
    }

    fun loadSimpleAccounts() {
        repository.getSimpleAccounts()
            .onEach { list ->
                state = state.copy(
                    simpleList = list,
                )
            }.launchIn(viewModelScope)
    }

    fun loadSimpleAndDeptAccounts() {
        repository.getSimpleAndDeptAccounts()
            .onEach { list ->
                state = state.copy(
                    debtAndSimpleList = list,
                )
            }.launchIn(viewModelScope)
    }


    fun addAccount(account: Account) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
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
}