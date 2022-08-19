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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.ui.theme.dividerColor

@Composable
fun TransactionAdder(category:  MutableState<Category>, addTransaction: (tr: Transaction)->Unit, closeAdder: ()->Unit, accountsList: List<Account>, categoriesList: List<Category>) {
    var sumText = remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val openChoseAccountDialog = remember { mutableStateOf(false) }
    val openChoseCategoryDialog = remember { mutableStateOf(false) }

    val transactionAccount = remember { mutableStateOf(AccountsData.accountsList[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffeeeeee))
            .height(510.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xff5c6bc0))
                        .clickable { openChoseAccountDialog.value = true }
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = "From account", fontSize = 14.sp, color=Color.White)
                            Text(text = transactionAccount.value.title, fontSize = 17.sp, color=Color.White, overflow = TextOverflow.Ellipsis)
                        }
                        Image(
                            painter = painterResource(id = transactionAccount.value.accountImg.img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(2.dp)
                                .width(55.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xffff4181))
                        .clickable { openChoseCategoryDialog.value = true }
                        .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = "To category", fontSize = 14.sp, color=Color.White)
                            Text(text = category.value.title, fontSize = 17.sp, color=Color.White, overflow = TextOverflow.Ellipsis)
                        }
                        Image(
                            painter = painterResource(id = category.value.categoryImg.img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(2.dp)
                                .width(36.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                        )
                    }
                }
            }

            Divider()
        }

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(2.dp),text = "Expense", color = Color(0xffff4181), fontSize = 14.sp)
            OutlinedTextField(
                value = sumText.value,
                onValueChange = {
                    sumText.value = if (it.isEmpty()) {
                        "0"
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> sumText.value //old value
                            else -> it   //new value
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Black),
                singleLine = true,
                maxLines = 1
            )
        }

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(2.dp),text = "Notes", color = Color(0xffff4181), fontSize = 14.sp)
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "Text...", textAlign = TextAlign.Center) },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(15.dp, 5.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Black),
                maxLines = 2
            )
        }

        Calculator(sumText)



        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff43a0e7)),
                onClick = {
                    val account = transactionAccount.value
                    val transactionNote: String? = if (note != "") note else null
                    val transaction = Transaction(
                        sum = sumText.value.toDoubleOrNull() ?: 0.0,
                        categoryUUID = category.value.uuid,
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

    ChooseTransactionAccountDialog(openDialog = openChoseAccountDialog, accountList = accountsList, transactionAccount = transactionAccount)
    ChooseTransactionCategoryDialog(openDialog = openChoseCategoryDialog, categoryList = categoriesList, transactionCategory = category)
}

@Composable
private fun Calculator(sumText: MutableState<String>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f).background(Color.LightGray),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.contains("÷|×|-|\\+".toRegex()))
                        sumText.value= evaluateSimpleMathExpr(sumText.value).toCalculatorString()
                    sumText.value+='÷'
                }
            ) {
                Text(text = "÷", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f).background(Color.LightGray),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.contains("÷|×|-|\\+".toRegex()))
                        sumText.value= evaluateSimpleMathExpr(sumText.value).toCalculatorString()
                    sumText.value+='×'
                }
            ) {
                Text(text = "×", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f).background(Color.LightGray),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.contains("÷|×|-|\\+".toRegex()))
                        sumText.value= evaluateSimpleMathExpr(sumText.value).toCalculatorString()
                    sumText.value+='-'
                }
            ) {
                Text(text = "-", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f).background(Color.LightGray),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.contains("÷|×|-|\\+".toRegex()))
                        sumText.value= evaluateSimpleMathExpr(sumText.value).toCalculatorString()
                    sumText.value+='+'
                }
            ) {
                Text(text = "+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='7'
                }
            ) {
                Text(text = "7", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='4'
                }
            ) {
                Text(text = "4", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='1'
                }
            ) {
                Text(text = "1", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(modifier = Modifier.weight(1f)) { Text(text = "$", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='8'
                }
            ) {
                Text(text = "8", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='5'
                }
            ) {
                Text(text = "5", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='2'
                }
            ) {
                Text(text = "2", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='0'
                }
            ) {
                Text(text = "0", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='9'
                }
            ) {
                Text(text = "9", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='6'
                }
            ) {
                Text(text = "6", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='3'
                }
            ) {
                Text(text = "3", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {sumText.value+='.'}
            ) {
                Text(text = ".", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f).background(Color.LightGray),
                onClick = {if(sumText.value.length>1) sumText.value=sumText.value.dropLast(1) else sumText.value="0"}
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            CalculatorButton(modifier = Modifier.weight(1f).background(Color.LightGray)) { Icon(imageVector = Icons.Default.DateRange, contentDescription = null) }
            if(sumText.value.contains("÷|×|-|\\+".toRegex())) {
                CalculatorButton(
                    modifier = Modifier.weight(2f).background(Color(0xff479ad6)),
                    onClick = {sumText.value = evaluateSimpleMathExpr(sumText.value).toCalculatorString()}
                ) {
                    Text(text = "=", fontSize = 22.sp, fontWeight = FontWeight.Bold, color=Color.White)
                }
            }
            else {
                CalculatorButton(
                    modifier = Modifier.weight(2f).background(Color(0xff479ad6)),
                    onClick = {}
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint=Color.White)
                }
            }
        }
    }
}

private fun Char.isMathOperator(): Boolean {
    return this=='÷' || this == '×' || this == '-' || this == '+'
}

private fun Double.toCalculatorString(): String {
    var str = String.format("%.2f", this)
    while(str.length>1 && (str.last()=='0'||str.last()=='.'))
        str = str.dropLast(1)
    return str
}

@Composable
private fun CalculatorButton(modifier: Modifier = Modifier, onClick: ()->Unit = {}, content: @Composable ()-> Unit) {
    Box(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.Gray))
            .fillMaxSize()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun evaluateSimpleMathExpr(mathExprStr: String): Double {
    val numbers = mathExprStr.split("÷|×|-|\\+".toRegex())
    return if(mathExprStr.contains('÷'))
        numbers[0].toDouble()/numbers[1].toDouble()
    else if(mathExprStr.contains('×'))
        numbers[0].toDouble()*numbers[1].toDouble()
    else if(mathExprStr.contains('-'))
        numbers[0].toDouble()-numbers[1].toDouble()
    else
        numbers[0].toDouble()+numbers[1].toDouble()
}



@Composable
private fun ChooseTransactionAccountDialog(openDialog: MutableState<Boolean>, accountList: List<Account>, transactionAccount: MutableState<Account>) {
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
                                    painter = painterResource(id = it.accountImg.img),
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

@Composable
private fun ChooseTransactionCategoryDialog(openDialog: MutableState<Boolean>, categoryList: List<Category>, transactionCategory: MutableState<Category>) {
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
                    items = categoryList,
                    itemContent = {
                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        transactionCategory.value = it
                                        openDialog.value = false
                                    }
                                    .background(
                                        if (it == transactionCategory.value) Color(
                                            0xff59aab3
                                        ) else Color.White
                                    )
                                    .padding(10.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = it.categoryImg.img),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(55.dp)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                                )
                                Text(it.title, fontSize = 16.sp)
                            }
                            Divider(modifier = Modifier.background(dividerColor))
                        }
                    }
                )
            }
        }
    }
}