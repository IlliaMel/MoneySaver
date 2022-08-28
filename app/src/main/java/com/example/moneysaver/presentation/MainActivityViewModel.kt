package com.example.moneysaver.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.AccountsData.currenciesList
import com.example.moneysaver.data.remote.CurrencyDto
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.repository.FinanceRepository
import com.example.moneysaver.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()


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

            loadCurrencyData()
            delay(400)
            _isLoading.value = false
        }
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
}
