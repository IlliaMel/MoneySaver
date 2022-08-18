package com.example.moneysaver.presentation.transactions.additional_composes

import androidx.compose.foundation.*
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
import com.example.moneysaver.ui.theme.whiteSurface

@Composable
fun TransactionItem(transaction: Transaction, categoryName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteSurface)
            .padding(0.dp, 4.dp, 0.dp, 4.dp),
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
                painter = painterResource(id = R.drawable.categories_icn),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(0.dp, 12.dp, 0.dp, 0.dp)
                    .width(65.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
            )

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(0.dp, 8.dp, 12.dp, 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(categoryName, color = Color.Black, fontSize = 17.sp)
                    val sumText: String = (if(transaction.sum>0) "+" else "-") + "$ " + Math.abs(transaction.sum)
                    val color = if(transaction.sum>0) Color.Green else Color.Red
                    Text(sumText, color = color, fontSize = 15.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("\uD83D\uDCB3 "+transaction.account.title, color = Color.Gray, fontSize = 16.sp)
                }
                (transaction.note)?.let{
                    Text(transaction.note, color = Color(0xffababab), fontSize = 14.sp)
                }
            }
        }
    }
}