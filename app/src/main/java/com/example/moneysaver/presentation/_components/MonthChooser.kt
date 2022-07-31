package com.example.moneysaver.presentation._components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.presentation.transactions.additional_composes.getNameOfMonthByNumber
import java.util.*

@Composable
fun MonthChooser(date: Date) {
    val monthName = getNameOfMonthByNumber(date.month);

    Row(
        modifier = Modifier.clickable { /* TODO */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date.date.toString(),
            modifier=Modifier.padding(4.dp, 0.dp).clip(RoundedCornerShape(2.dp)).background(Color.White).padding(1.dp,0.dp, 0.dp, 0.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.Black
        )
        Text(
            text = monthName.uppercase() + " "+ date.year.toString(),
            modifier=Modifier.padding(4.dp, 0.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.White
        )
        Text(
            text = "▾",
            modifier=Modifier.padding(4.dp, 0.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.White
        )
    }

}