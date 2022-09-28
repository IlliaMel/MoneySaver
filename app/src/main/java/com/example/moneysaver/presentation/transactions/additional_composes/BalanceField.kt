package com.example.moneysaver.presentation.transactions.additional_composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.ui.theme.*
import kotlin.math.abs

@Composable
fun BalanceField(modifier: Modifier = Modifier, account: Account, text: String, balance: Double) {
    Column(
        modifier = modifier
            .padding(8.dp).background(backgroundPrimaryColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            maxLines = 1,
            color = textPrimaryColor,
            overflow = TextOverflow.Ellipsis
        )
        val balanceText: String = (if(balance>0) "+" else (if(balance < 0) "-" else ""))+account.currencyType.currency + " "+ abs(balance)
        val balanceColor = if(balance>0) currencyColor else (if(balance < 0) currencyColorSpent else currencyColorZero)
        Text(
            text = balanceText,
            color = balanceColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}