package com.example.moneysaver.presentation._components.navigation_drawer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Currency

@Composable
fun WithdrawalDepositField(
    selectedAccountFirst: MutableState<Account>,
    selectedCurrencyTypeFirst: MutableState<Currency>,
    sumTextFirst: MutableState<String>,
    selectedAccountSecond: MutableState<Account>,
    selectedCurrencyTypeSecond: MutableState<Currency>,
    sumTextSecond: MutableState<String>,
    isFirstFieldSelected: MutableState<Boolean>
) {

    Column(
        modifier = Modifier.fillMaxWidth().height(70.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Divider()

        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (isFirstFieldSelected.value) selectedAccountFirst.value.accountImg.externalColor.copy(
                            alpha = 0.3f
                        )
                        else Color.White
                    )
                    .clickable { isFirstFieldSelected.value = true },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(modifier = Modifier.padding(4.dp),text = "Withdrawal", color = Color.Black, fontSize = 16.sp)
                Text(modifier = Modifier.padding(2.dp),text =  sumTextFirst.value + " " + selectedCurrencyTypeFirst.value.currency, color = selectedAccountFirst.value.accountImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.width(1.dp).fillMaxHeight())

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (!isFirstFieldSelected.value) selectedAccountSecond.value.accountImg.externalColor.copy(
                            alpha = 0.3f
                        )
                        else Color.White
                    )
                    .clickable { isFirstFieldSelected.value = false },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(modifier = Modifier.padding(4.dp),text = "Deposit", color = Color.Black, fontSize = 16.sp)
                Text(modifier = Modifier.padding(2.dp),text =  sumTextSecond.value + " " + selectedCurrencyTypeSecond.value.currency, color = selectedAccountSecond.value.accountImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

        }

        Divider()
    }

}