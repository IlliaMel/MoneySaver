package com.example.moneysaver.presentation._components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.ui.theme.dividerColor

@Composable
fun TransactionAdder(category: Category, addTransaction: (tr: Transaction)->Unit, closeAdder: ()->Unit) {
    var sumText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val openChoseAccountDialog = remember { mutableStateOf(false) }
    val transactionAccount = remember { mutableStateOf(AccountsData.accountsList[0]) }

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
                    .clickable { openChoseAccountDialog.value = true }
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "From account", fontSize = 16.sp)
                Text(text = transactionAccount.value.title, fontSize = 16.sp)
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
                sumText = if (it.isEmpty()) {
                    it
                } else {
                    when (it.toDoubleOrNull()) {
                        null -> sumText //old value
                        else -> it   //new value
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
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
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff43a0e7)),
                onClick = {
                    val account = transactionAccount.value
                    val transactionNote: String? = if (note != "") note else null
                    val transaction = Transaction(
                        sum = sumText.toDoubleOrNull() ?: 0.0,
                        category = category,
                        account = account,
                        note = transactionNote
                    )
                    addTransaction(transaction)
                    closeAdder()
                }) {
                Text("OK")
            }
        }
    }

    ChooseTransactionAccountDialog(openDialog = openChoseAccountDialog, accountList = AccountsData.accountsList, transactionAccount = transactionAccount)
    
}


@Composable
fun ChooseTransactionAccountDialog(openDialog: MutableState<Boolean>, accountList: List<Account>, transactionAccount: MutableState<Account>) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(350.dp)
                    .width(300.dp)
                    .background(Color.White)
            ) {
                items(
                    items = accountList,
                    itemContent = {
                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        transactionAccount.value = it
                                        openDialog.value = false
                                    }
                                    .background(if (it == transactionAccount.value) Color(0xff59aab3) else Color.White)
                                    .padding(10.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = it.accountImg),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(55.dp)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                                )
                                Column() {
                                    Text(it.title, fontSize = 16.sp)
                                    val balanceText: String = (if(it.balance>0) "+" else (if(it.balance < 0) "-" else ""))+"$ "+it.balance
                                    val balanceColor = if(it.balance>0) Color.Green else (if(it.balance < 0) Color.Red else Color.Gray)
                                    Text(text = balanceText, color = balanceColor, fontSize = 14.sp)
                                }
                            }
                            Divider(modifier = Modifier.background(dividerColor))
                        }
                    }
                )
            }
        }
    }
}