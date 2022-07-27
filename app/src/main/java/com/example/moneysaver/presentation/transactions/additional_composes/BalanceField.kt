package com.example.moneysaver.presentation.transactions.additional_composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BalanceField(text: String,balance: Double) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text)
        val balanceText: String = (if(balance>0) "+" else (if(balance < 0) "-" else ""))+"$ "+balance
        val balanceColor = if(balance>0) Color.Green else (if(balance < 0) Color.Red else Color.Gray)
        Text(balanceText, color = balanceColor)
    }
}