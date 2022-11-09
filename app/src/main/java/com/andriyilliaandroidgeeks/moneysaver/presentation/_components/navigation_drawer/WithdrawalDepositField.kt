package com.andriyilliaandroidgeeks.moneysaver.presentation._components.navigation_drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.backgroundSecondaryColor
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.bordersSecondaryColor
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.textPrimaryColor

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
        Divider(modifier = Modifier.height(0.7.dp).background(bordersSecondaryColor))

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
                        else backgroundSecondaryColor
                    )
                    .clickable { isFirstFieldSelected.value = true },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(modifier = Modifier.padding(4.dp),text = stringResource(R.string.withdrawal), color = textPrimaryColor, fontSize = 16.sp)
                Text(modifier = Modifier.padding(2.dp),text =  sumTextFirst.value + " " + selectedCurrencyTypeFirst.value.currency, color = selectedAccountFirst.value.accountImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            // Divider(modifier = Modifier.width(1.dp).fillMaxHeight().background(bordersSecondaryColor))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (!isFirstFieldSelected.value) selectedAccountSecond.value.accountImg.externalColor.copy(
                            alpha = 0.3f
                        )
                        else backgroundSecondaryColor
                    )
                    .clickable { isFirstFieldSelected.value = false },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(modifier = Modifier.padding(4.dp),text = stringResource(R.string.deposit), color = textPrimaryColor, fontSize = 16.sp)
                Text(modifier = Modifier.padding(2.dp),text =  sumTextSecond.value + " " + selectedCurrencyTypeSecond.value.currency, color = selectedAccountSecond.value.accountImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

        }

        Divider(modifier = Modifier.background(bordersSecondaryColor))
    }

}