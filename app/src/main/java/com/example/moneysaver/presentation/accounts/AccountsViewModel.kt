package com.example.moneysaver.presentation.accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.domain.account.Account

class AccountsViewModel(): ViewModel() {

    var state by mutableStateOf(AccountsState())
        private set

    fun loadAccounts() {
        state = state.copy(
            accountList = AccountsData.accountsList,
            currentAccount = AccountsData.accountsList.get(0)
        ) }

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
        state.accountList.let{
            it.forEach(){
                if(it.isForGoal){
                    sum+=it.balance
                }
            }
        }
        return sum
    }
}