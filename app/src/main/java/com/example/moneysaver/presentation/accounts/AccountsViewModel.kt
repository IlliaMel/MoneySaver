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
        loadAccounts()
        loadGoalAccounts()
    }


    var state by mutableStateOf(AccountsState())
        private set


    private fun loadAccounts() {
        repository.getAccounts()
            .onEach { list ->
                state = state.copy(
                    accountList = list,
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


    fun addAccount(account: Account) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
        loadAccounts()
    }

    fun loadBankAccountSum() : Double{
        var sum : Double = 0.0
        state.accountList.let{
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