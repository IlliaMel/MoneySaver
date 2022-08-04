package com.example.moneysaver.presentation.categories.additional_composes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.presentation.categories.CategoriesViewModel

@Composable
fun TransactionAdder(category: Category, viewModel: CategoriesViewModel, closeAdder: ()->Unit) {
    var sumText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

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
                Text(text = "To Category", fontSize = 16.sp)
                Text(text = category.title, fontSize = 16.sp)
            }
        }

        Divider()
        OutlinedTextField(
            value = sumText,
            onValueChange = {
                sumText = if (it.isEmpty()){
                    it
                } else {
                    when (it.toDoubleOrNull()) {
                        null -> sumText //old value
                        else -> it   //new value
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            placeholder = { Text("Sum") },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Black)
        )

        Divider()
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            placeholder = { Text("Notes...") },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Black)
        )

        Divider()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                shape = RoundedCornerShape(30.dp),
                colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xff43a0e7)),
                onClick = {
                val transactionAccount = Account(title="TestAccount")
                val transactionNote: String? = if(note!="") note else null
                val transaction = Transaction(
                    sum = sumText.toDoubleOrNull()?: 0.0,
                    category = category,
                    account = transactionAccount,
                    note = transactionNote
                )
                viewModel.addTransaction(transaction)
                closeAdder()
            }) {
                Text("OK")
            }
        }

    }
}