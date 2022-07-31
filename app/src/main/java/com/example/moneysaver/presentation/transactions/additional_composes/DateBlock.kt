package com.example.moneysaver.presentation.transactions.additional_composes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun DateBlock(date: Date, balanceChange: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffececec))
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = date.date.toString(), color = Color(0xff7d7d7d), fontSize = 26.sp, fontWeight = FontWeight.Bold);
            Column(
                modifier = Modifier
                    .padding(12.dp, 0.dp, 0.dp, 0.dp)
                    .offset(x = 0.dp, y = (-2).dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = getNameOfDayByDate(date).uppercase(), color=Color(0xffababab), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = getNameOfMonthByNumber(date.month).uppercase() + " "+date.year, color = Color(0xff7d7d7d), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val balanceChangeText: String = (if(balanceChange>0) "+" else (if(balanceChange < 0) "-" else ""))+"$ "+balanceChange
            val balanceChangeColor = if(balanceChange>0) Color.Green else (if(balanceChange < 0) Color.Red else Color.Gray)
            Text(text = balanceChangeText, color = balanceChangeColor, fontSize = 20.sp)
        }
    }
}

fun getNameOfMonthByNumber(monthNumber: Int): String {
    return when(monthNumber) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "Augusts"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        else -> "December"
    }
}

fun getNameOfDayByDate(date: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return when(calendar[Calendar.DAY_OF_WEEK]) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> "Sunday"
    }
}