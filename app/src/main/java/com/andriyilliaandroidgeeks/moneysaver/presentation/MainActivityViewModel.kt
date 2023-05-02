package com.andriyilliaandroidgeeks.moneysaver.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.CategoriesData
import com.andriyilliaandroidgeeks.moneysaver.data.remote.CurrencyDto
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction
import com.andriyilliaandroidgeeks.moneysaver.domain.repository.FinanceRepository
import com.andriyilliaandroidgeeks.moneysaver.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    var account by mutableStateOf(AccountsData.allAccountFilter)

    var isParsingSucceeded = mutableStateOf(false)

    var state by mutableStateOf(MainActivityState())
        private set

    init {

        viewModelScope.launch {
            var resultData: CurrencyDto? = null
            if(MainActivity.isNeedToParseCurrency){
                when(val result = financeRepository.getCurrencyData("USD", "EUR,USD,UAH,GBP,CNY,AED,TRY,BTC")) {
                    is Resource.Success -> {
                        isParsingSucceeded.value = true
                        resultData = result.data
                        financeRepository.deleteAll()
                        putInDbCurrencyData(resultData)

                    }
                    is Resource.Error -> {
                        resultData = result.data
                    }
                }
            }

            val BACKIMG = "background_img"
            val photoUri = MainActivity.instance?.getPreferences(Context.MODE_PRIVATE)?.getString(BACKIMG, "")
            if (photoUri != "") {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                MainActivity.instance?.grantUriPermission(
                    MainActivity.instance?.packageName,
                    Uri.parse(photoUri),
                    takeFlags
                );
                /* val file = File(photoUri!!);
                 if (!file.exists()){
                     with(sharedPref.edit()) {
                         putString(BACKIMG, "")
                         apply()
                     }
                     viewModel.updateAccountBackImg("")
                 } else
                     */
                updateAccountBackImg(context = MainActivity.instance!!, Uri.parse(photoUri))
            }

            loadCurrencyData()
            delay(400)

        }

        loadCategories()
        loadCategoriesData()
        loadCurrencyData()
        val tempDate: MutableState<Date?> = mutableStateOf(null)
        loadTransactions(tempDate, tempDate)
        loadAccounts()

    }
/*
    private fun loadCurrencyData() {
        financeRepository.getCurrencyTypes()
            .onEach { list ->
                state = state.copy(
                    currenciesList = list,
                )
                AccountsData.currenciesList = list
            }.launchIn(viewModelScope)
    }
    */

    fun returnCurrencyValue(which : String , to : String) : Double {
        var whichFound = state.currenciesList.find { it.currencyName == which }
        var toFound = state.currenciesList.find { it.currencyName == to }
        return (toFound?.valueInMainCurrency ?: 1.0) / (whichFound?.valueInMainCurrency ?: 1.0)
    }

   fun isCurrencyDbEmpty() : Boolean{
        return state.currenciesList.isEmpty()
    }


    private suspend fun putInDbCurrencyData(resultData: CurrencyDto?){

        resultData?.rates?.let {
            Currency(
                description = "European Euro",
                currency = "€",
                currencyName = "EUR",
                valueInMainCurrency = it.eUR
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

        resultData?.rates?.let {
            Currency(
                description = "USA Dollar",
                currency = "$",
                currencyName = "USD",
                valueInMainCurrency = it.uSD
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

        resultData?.rates?.let {
            Currency(
                description = "Ukrainian Hryvnia",
                currency = "₴",
                currencyName = "UAH",
                valueInMainCurrency = it.uAH
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

        resultData?.rates?.let {
            Currency(
                description = "Pound",
                currency = "£",
                currencyName = "GBP",
                valueInMainCurrency = it.gBP
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

        resultData?.rates?.let {
            Currency(
                description = "Chinese Yuan",
                currency = "¥",
                currencyName = "CNY",
                valueInMainCurrency = it.cNY
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

        resultData?.rates?.let {
            Currency(
                description = "UAE Dirham",
                currency = "د.إ",
                currencyName = "AED",
                valueInMainCurrency = it.aED
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

        resultData?.rates?.let {
            Currency(
                description = "Turkish Lira",
                currency = "₺",
                currencyName = "TRY",
                valueInMainCurrency = it.tRY
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }


        resultData?.rates?.let {
            Currency(
                description = "Bitcoin",
                currency = "₿",
                currencyName = "BTC",
                valueInMainCurrency = it.bTC
            )
        }?.let {
            financeRepository.insertCurrencyType(
                it
            )
        }

    }


// ###########################################################

    private fun loadCurrencyData() {
        financeRepository.getCurrencyTypes()
            .onEach { list ->
                state = state.copy(
                    currenciesList = list,
                )
                AccountsData.currenciesList = list
            }.launchIn(viewModelScope)
    }


    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove spending data if transaction isn't new
            val transactionAccount = financeRepository.getAccountByUUID(transaction.accountUUID)
            transactionAccount?.let {
                val updatedAccount = transactionAccount!!.copy(
                    balance= transactionAccount.balance - transaction.sum
                )
                // update account
                financeRepository.insertAccount(updatedAccount)
            }

            if(transaction.toAccountUUID != null && transactionAccount != null){
                val transactionToAccount = financeRepository.getAccountByUUID(transaction.toAccountUUID)
                val updatedToAccount = transactionToAccount!!.copy(
                    balance = transactionToAccount.balance - transaction.toAccountSum!!
                )
                financeRepository.insertAccount(updatedToAccount)
            }

            financeRepository.deleteTransaction(transaction)

        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // remove previous spending data if transaction isn't new
            financeRepository.getTransactionByUUID(transaction.uuid)?.let {
                financeRepository.deleteTransaction(transaction)
            }

            // add new spending data

            val transactionAccount = financeRepository.getAccountByUUID(transaction.accountUUID)

            val updatedAccount = transactionAccount!!.copy(
                balance = transactionAccount.balance+transaction.sum
            )
            financeRepository.insertAccount(updatedAccount)


            if(transaction.toAccountUUID != null){
                val transactionToAccount = financeRepository.getAccountByUUID(transaction.toAccountUUID)
                val updatedToAccount = transactionToAccount!!.copy(
                    balance = transactionToAccount.balance + transaction.toAccountSum!!
                )
                financeRepository.insertAccount(updatedToAccount)
            }

            // add or update transaction
            financeRepository.insertTransaction(transaction)
        }
    }

    fun loadCategories() {
        financeRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list as MutableList<Category>,
                )
            }.launchIn(viewModelScope)
    }
    fun ifAllCategoriesIsZero() : Boolean{
        state.categoriesList.forEach{
            if(it.spent != 0.0)
                return false
        }
        return true
    }

    fun loadCategoriesData() {
        financeRepository.getTransactions().onEach { transactions ->
            val categories = state.categoriesList
            for(category in categories) {
                category.spent = 0.0
                for(tr in transactions) {
                    if(tr.accountUUID == account.uuid || (account.isForGoal && account.isForDebt))
                        if(tr.categoryUUID == category.uuid) {
                            category.spent-=tr.sum
                        }
                }
            }
            state = state.copy(
                categoriesList = categories,
                selectedAccount = account
            )
        }.launchIn(viewModelScope)
    }

    fun addingTransactionIsAllowed(): Boolean {
        return state.accountsList.isNotEmpty()
                && state.categoriesList.isNotEmpty()
                && state.categoriesList[0].uuid!= CategoriesData.addCategory.uuid
    }

    fun loadCategoriesDataInDateRange(minDate: Date, maxDate: Date) {
        financeRepository.getTransactionsInDateRange(minDate, maxDate).onEach { transactions ->
            val categories = state.categoriesList
            for(category in categories) {
                category.spent = 0.0
                for(tr in transactions) {
                    if(tr.accountUUID == account.uuid || (account.isForGoal && account.isForDebt))
                        if(tr.categoryUUID == category.uuid) {
                            category.spent-=tr.sum
                        }
                }
            }
            state = state.copy(
                categoriesList = categories,
                selectedAccount = account
            )
        }.launchIn(viewModelScope)
    }

    fun loadAccounts() {
        financeRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    accountsList = list
                )
            }.launchIn(viewModelScope)
    }

// ###########################################################

    fun loadTransactions(minDate: MutableState<Date?>, maxDate: MutableState<Date?>) {
        financeRepository.getTransactions()
            .onEach { list ->
                var resultList = list.filter {  it.accountUUID == account.uuid || it.toAccountUUID == account.uuid || (account.isForGoal && account.isForDebt) }
                var startingBalance = if(!(account.isForGoal && account.isForDebt)) financeRepository.getAccountByUUID(account.uuid)!!.balance else account.balance
                var endingBalance = if(!(account.isForGoal && account.isForDebt)) financeRepository.getAccountByUUID(account.uuid)!!.balance else account.balance
                for(transaction in resultList) {
                    if(maxDate.value!=null && transaction.date > maxDate.value) {
                        if(!(account.isForGoal && account.isForDebt)) {
                            if(account.uuid==transaction.uuid) {
                                endingBalance -= transaction.sum*returnCurrencyValue(state.accountsList.find { it.uuid == transaction.accountUUID }?.currencyType!!.currencyName, account.currencyType.currencyName )
                            } else if(account.uuid==transaction.toAccountUUID) {
                                endingBalance -= transaction.toAccountSum!!*returnCurrencyValue(state.accountsList.find { it.uuid == transaction.toAccountUUID }?.currencyType!!.currencyName, account.currencyType.currencyName )
                            }
                        }
                    }
                    if(minDate.value==null || transaction.date > minDate.value) {
                        if(!(account.isForGoal && account.isForDebt)) {
                            if(account.uuid==transaction.accountUUID) {
                                startingBalance -= transaction.sum*returnCurrencyValue(state.accountsList.find { it.uuid == transaction.accountUUID }?.currencyType!!.currencyName, account.currencyType.currencyName )
                            } else if(account.uuid==transaction.toAccountUUID) {
                                startingBalance -= transaction.toAccountSum!!*returnCurrencyValue(state.accountsList.find { it.uuid == transaction.toAccountUUID }?.currencyType!!.currencyName, account.currencyType.currencyName )
                            }
                        }

                    }
                }
                startingBalance=((startingBalance * 100.0).roundToInt() / 100.0)
                endingBalance=((endingBalance * 100.0).roundToInt() / 100.0)

                if(minDate.value!=null && maxDate.value!=null) {
                    resultList = resultList.filter { it.date >= minDate.value!! && it.date <= maxDate.value }
                }

                state = state.copy(
                    transactionList = resultList,
                    startingBalance = startingBalance,
                    endingBalance = endingBalance
                )
            }.launchIn(viewModelScope)
    }

/*
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
    */

    fun deleteTransactions() {
        viewModelScope.launch {
            financeRepository.deleteAllTransaction()
        }
    }
/*
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

    fun loadCategories() {
        financeRepository.getCategories()
            .onEach { list ->
                state = state.copy(
                    categoriesList = list
                )
            }.launchIn(viewModelScope)
    }

    fun loadAccounts() {
        financeRepository.getAllAccounts()
            .onEach { list ->
                state = state.copy(
                    accountsList = list
                )
            }.launchIn(viewModelScope)
    }
*/
    fun getCategoryNameByUUIID(uuid: UUID): String? = runBlocking {
        financeRepository.getCategoryByUUID(uuid)?.title
    }
/*
    fun addingTransactionIsAllowed(): Boolean {
        return state.accountsList.isNotEmpty()
                && state.categoriesList.isNotEmpty()
                && state.categoriesList[0].uuid!= CategoriesData.addCategory.uuid
    }
    */

    fun importRepository(
        accounts: List<Account>,
        categories: List<Category>,
        transactions: List<Transaction>
    ) {
        viewModelScope.launch {
            state = state.copy(isDataLoading = true)
            financeRepository.deleteAllTransaction()
            financeRepository.deleteAllCategories()
            financeRepository.deleteAllAccounts()
            for(account in accounts) financeRepository.insertAccount(account)
            for(category in categories) financeRepository.insertCategory(category)
            for(transaction in transactions) financeRepository.insertTransaction(transaction)
            state = state.copy(isDataLoading = false)
        }
    }

    fun updateAccountBackImg(context: Context, uri: Uri) {
        var loadedBitmap: Bitmap? = null
        viewModelScope.launch {
            loadedBitmap = loadImageAsBitmap(context, uri)
            state = state.copy(accountBgImgBitmap = loadedBitmap)
            _isLoading.value = false
        }

    }

    private suspend fun loadImageAsBitmap(context: Context, uri: Uri): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(uri)
            .build()

        return withContext(Dispatchers.IO) {
            request.context.imageLoader.execute(request).drawable?.toBitmap()
        }
    }
}
