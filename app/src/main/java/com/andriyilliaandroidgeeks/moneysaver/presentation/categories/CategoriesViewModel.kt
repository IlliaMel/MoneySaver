package com.andriyilliaandroidgeeks.moneysaver.presentation.categories

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.CategoriesData
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.repository.FinanceRepository
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {



    var account by mutableStateOf(AccountsData.allAccountFilter)

    var state by mutableStateOf(CategoriesState())
        private set

    init {
        loadCategories()
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

    fun addCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.insertCategory(category)
        }
    }
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            financeRepository.deleteTransactionsByCategoryUUID(category.uuid)
            financeRepository.deleteCategory(category)
        }
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

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove previous spending data if transaction isn't new
            financeRepository.getTransactionByUUID(transaction.uuid)?.let {
                val transactionAccount = financeRepository.getAccountByUUID(it.accountUUID)
                val updatedAccount = transactionAccount!!.copy(
                    balance = transactionAccount.balance-it.sum
                )
                // update account
                financeRepository.insertAccount(updatedAccount)
            }
            // add new spending data
            val transactionAccount = financeRepository.getAccountByUUID(transaction.accountUUID)
            val updatedAccount = transactionAccount!!.copy(
                balance = transactionAccount.balance+transaction.sum
            )
            financeRepository.insertAccount(updatedAccount)

            // add or update transaction
            financeRepository.insertTransaction(transaction)
        }
    }
    fun getListWithAdderCategory(isAddingCategory : Boolean, isForEditing : Boolean) : List<Category> {
        var list = state.categoriesList.filter { it.isForSpendings == state.isForSpendings }.toMutableList()
        if(!isAddingCategory && !isForEditing) {
            if (list.isNotEmpty() && list.last().uuid == CategoriesData.addCategory.uuid) {

            } else
                list.add(CategoriesData.addCategory)
        }else if(list.isNotEmpty() && list.last().uuid == CategoriesData.addCategory.uuid)
            list.removeLast()

        return list.toList()
    }

    fun loadCategories() {
        financeRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list,
                    spend = 0.0,
                    earned = 0.0
                )
            }.launchIn(viewModelScope)
    }

    fun swapCategoriesType() {
        state = state.copy(
            isForSpendings = !state.isForSpendings
        )
    }

    fun ifAllCategoriesIsZero() : Boolean{
        state.categoriesList.filter { it.isForSpendings == state.isForSpendings }.forEach{
            if(it.spent != 0.0)
                return false
        }
        return true
    }




    fun loadCategoriesDataInDateRange(minDate: Date?, maxDate: Date?, base: String) {

        (if(minDate!=null||maxDate!=null)
            financeRepository.getTransactionsInDateRange(minDate!!, maxDate!!)
        else
            financeRepository.getTransactions()).onEach { transactions ->
            val categories = state.categoriesList
            var earned = 0.0
            var spend = 0.0
            for(category in categories) {
                category.spent = 0.0
                for(tr in transactions) {
                    if(tr.accountUUID == account.uuid || (account.isForGoal && account.isForDebt))
                        if(category!=null && state.accountsList.isNotEmpty())
                        if(tr.categoryUUID == category.uuid) {
                            if(state.accountsList.isNotEmpty())
                            if(tr.sum < 0){
                                spend += tr.sum * returnCurrencyValue(state.accountsList.find { it.uuid == tr.accountUUID }?.currencyType!!.currencyName, base )
                            }else
                                earned += tr.sum * returnCurrencyValue(state.accountsList.find { it.uuid == tr.accountUUID }?.currencyType!!.currencyName, base )
                            category.spent += Math.round(100 * tr.sum * returnCurrencyValue(state.accountsList.find { it.uuid == tr.accountUUID }?.currencyType!!.currencyName,state.categoriesList.find { it.uuid == tr.categoryUUID }?.currencyType!!.currencyName)) / 100.0
                    }
                }
            }
            state = state.copy(
                categoriesList = categories,
                selectedAccount = account,
                spend = Math.round(spend * 100) / 100.0,
                earned = Math.round(earned * 100) / 100.0
            )
          //  if(!isCategoriesParsed)
         //       delay(100)
        //    isCategoriesParsed = true
        }.launchIn(viewModelScope)
    }

    fun returnCurrencyValue(which : String , to : String) : Double {
        var whichFound = state.currenciesList.find { it.currencyName == which }
        var toFound = state.currenciesList.find { it.currencyName == to }
        return (toFound?.valueInMainCurrency ?: 1.0) / (whichFound?.valueInMainCurrency ?: 1.0)
    }

    fun addingTransactionIsAllowed(): Boolean {
        return state.accountsList.isNotEmpty()
                && state.categoriesList.isNotEmpty()
                && state.categoriesList[0].uuid!= CategoriesData.addCategory.uuid
    }

    fun loadAccounts() {
        financeRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    accountsList = list
                )
            }.launchIn(viewModelScope)
    }

}