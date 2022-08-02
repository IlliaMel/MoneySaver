package com.example.moneysaver.presentation.categories.additional_composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.domain.category.Category

@Composable
fun TransactionAdder(category: Category) {
    var sum by remember { mutableStateOf(0.0) }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffeeeeee))
            .height(300.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "From account", fontSize = 16.sp)
                Text(text = "Account", fontSize = 16.sp)
            }
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "To category", fontSize = 16.sp)
                Text(text = "Category", fontSize = 16.sp)
            }
        }

        Divider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Expense", fontSize = 16.sp)
            TextField(
                value = sum.toString(),
                onValueChange = { sum = it.toDouble() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(10.dp)
            )
        }

        Divider()
        TextField(
            value = note,
            onValueChange = { note = it },
            placeholder = { Text("Notes...") },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Divider()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { /*TODO*/ }) {
                Text("OK")
            }
        }

    }
}