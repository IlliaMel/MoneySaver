package com.example.moneysaver.presentation.transactions.additional_composes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.domain.transaction.Transaction

@Composable
fun TransactionItem(transaction: Transaction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 1.dp, 0.dp, 1.dp),
        //.background(whiteSurface)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .clickable { },
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
        ) {

            Image(
                painter = painterResource(id = R.drawable.cash),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(0.dp, 12.dp, 12.dp, 8.dp)
                    .width(65.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
            )

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(0.dp, 4.dp, 12.dp, 2.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(transaction.category, color = Color.Black, fontSize = 19.sp)
                    val sumText: String = (if(transaction.isIncome) "+" else "-") + "$ " + transaction.sum
                    val color = if(transaction.isIncome) Color.Green else Color.Red
                    Text(sumText, color = color, fontSize = 19.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(transaction.account, color = Color.Gray, fontSize = 17.sp)
                }
                (transaction.note)?.let{
                    Text(transaction.note, color = Color.Gray, fontSize = 15.sp)
                }
            }
        }
    }
}