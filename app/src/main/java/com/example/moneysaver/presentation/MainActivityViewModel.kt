package com.example.moneysaver.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysaver.domain.repository.CurrencyRepository
import com.example.moneysaver.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(400)
            _isLoading.value = false

            when(val result = repository.getCurrencyData("USD", "EUR,USD,UAH,GBP,CNY,AED,TRY,BTC")) {
                is Resource.Success -> {
                    var resultMap = result.toString()
                }
                is Resource.Error -> {
                    var resultMap = result
                }
            }

        }
    }

}