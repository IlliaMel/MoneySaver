package com.example.moneysaver.presentation._components

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
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
import com.example.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.example.moneysaver.ui.theme.dividerColor
import java.util.*

@Composable
fun TransactionEditor(
    currentTransaction: Transaction? = null,
    category:  MutableState<Category>,
    addTransaction: (tr: Transaction)->Unit,
    deleteTransaction: (tr: Transaction)->Unit = {},
    closeAdder: ()->Unit, accountsList: List<Account>,
    categoriesList: List<Category>
) {

    var choiceIsActive = remember { mutableStateOf(currentTransaction!=null)}

    var sumText = remember { mutableStateOf(currentTransaction?.sum?.toCalculatorString() ?: "0") }
    var date: MutableState<Date?> = remember { mutableStateOf(currentTransaction?.date ?: Date()) }
    var note by remember { mutableStateOf(currentTransaction?.note ?: "") }
    val transactionAccount = remember { mutableStateOf(if(currentTransaction!=null) accountsList.first{it.uuid == currentTransaction.accountUUID} else if(accountsList.isNotEmpty()) accountsList[0] else AccountsData.accountsList[0]) }
    var isSubmitted = remember { mutableStateOf(false) }
    val openPickDateDialog = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val openChoseAccountDialog = remember { mutableStateOf(false) }
    val openChoseCategoryDialog = remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffeeeeee))
            .height(460.dp),
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
                        .background(transactionAccount.value.accountImg.externalColor)
                        .clickable { if(!choiceIsActive.value) openChoseAccountDialog.value = true }
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
                        .background(category.value.categoryImg.externalColor)
                        .clickable { if(!choiceIsActive.value) openChoseCategoryDialog.value = true }
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
            Text(modifier = Modifier.padding(2.dp),text = "Expense", color = category.value.categoryImg.externalColor, fontSize = 16.sp)
            Text(modifier = Modifier.padding(2.dp),text = "$ "+ sumText.value, color = category.value.categoryImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                enabled = !choiceIsActive.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "Notes...", textAlign = TextAlign.Center) },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = transactionAccount.value.accountImg.externalColor
                ),
                maxLines = 1
            )
        }

        if(choiceIsActive.value) {
            Row(modifier = Modifier
                .padding(6.dp, 0.dp)
                .fillMaxWidth()
                .height(240.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(90.dp)
                        .height(90.dp)
                        .background(Color(0xffc8ccc9))
                        .clickable {
                                    if(currentTransaction!=null)
                                        deleteTransaction(currentTransaction!!)
                                    closeAdder()
                                   },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(modifier = Modifier.width(40.dp).height(40.dp), imageVector = Icons.Filled.Delete, contentDescription = null, tint = Color(0xffe31d0b))
                    Text(text = "Delete", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier
                    .clip(CircleShape)
                    .width(90.dp)
                    .height(90.dp)
                    .background(Color(0xffc8ccc9))
                    .clickable {
                        choiceIsActive.value = false
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(modifier = Modifier.width(40.dp).height(40.dp), imageVector = Icons.Filled.Edit, contentDescription = null, tint = Color(0xff0b53e3))
                    Text(text = "Edit", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Calculator(sumText, isSubmitted, openPickDateDialog, focusManager)
        }

        Row(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
                .border(BorderStroke(1.dp, Color(0xff54514d))),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = getShortDateString(date.value?:Date()), color = Color(0xff54514d), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

    }

    if(isSubmitted.value) {
        val transactionNote: String? = if (note != "") note else null
        val transaction = if(currentTransaction==null) Transaction(
            sum = sumText.value.toDoubleOrNull() ?: 0.0,
            categoryUUID = category.value.uuid,
            accountUUID = transactionAccount.value.uuid,
            date = date.value?:Date(),
            note = transactionNote
        ) else Transaction(
            uuid=currentTransaction.uuid,
            sum = sumText.value.toDoubleOrNull() ?: 0.0,
            categoryUUID = category.value.uuid,
            accountUUID = transactionAccount.value.uuid,
            date = date.value?:Date(),
            note = transactionNote
        )
        addTransaction(transaction)
        isSubmitted.value=false
        sumText.value="0"
        closeAdder()
    }

    DatePickerDialog(openDialog = openPickDateDialog, startDate = date)
    ChooseTransactionAccountDialog(openDialog = openChoseAccountDialog, accountList = accountsList, transactionAccount = transactionAccount)
    ChooseTransactionCategoryDialog(openDialog = openChoseCategoryDialog, categoryList = categoriesList, transactionCategory = category)
}

@Composable
private fun Calculator(sumText: MutableState<String>, isSubmitted: MutableState<Boolean>, openDatePickerDialog: MutableState<Boolean>, focusManager: FocusManager) {
    Row(modifier = Modifier
        .padding(6.dp, 0.dp)
        .fillMaxWidth()
        .height(240.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='÷'
                },
                bgColor = Color(0xff939694),
                focusManager = focusManager
            ) {
                Text(text = "÷", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='×'
                },
                bgColor = Color(0xff939694),
                focusManager = focusManager
            ) {
                Text(text = "×", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='-'
                },
                bgColor = Color(0xff939694),
                focusManager = focusManager
            ) {
                Text(text = "-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='+'
                },
                bgColor = Color(0xff939694),
                focusManager = focusManager
            ) {
                Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='7'
                },
                focusManager = focusManager
            ) {
                Text(text = "7", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='4'
                },
                focusManager = focusManager
            ) {
                Text(text = "4", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='1'
                },
                focusManager = focusManager
            ) {
                Text(text = "1", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(modifier = Modifier.weight(1f), focusManager = focusManager) { Text(text = "$", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='8'
                },
                focusManager = focusManager
            ) {
                Text(text = "8", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='5'
                },
                focusManager = focusManager
            ) {
                Text(text = "5", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='2'
                },
                focusManager = focusManager
            ) {
                Text(text = "2", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='0'
                },
                focusManager = focusManager
            ) {
                Text(text = "0", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='9'
                },
                focusManager = focusManager
            ) {
                Text(text = "9", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='6'
                },
                focusManager = focusManager
            ) {
                Text(text = "6", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='3'
                },
                focusManager = focusManager
            ) {
                Text(text = "3", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {sumText.value+='.'},
                focusManager = focusManager
            ) {
                Text(text = ".", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.length>1) sumText.value=sumText.value.dropLast(1) else sumText.value="0"
                    if(sumText.value=="-") sumText.value="0"
                },
                bgColor = Color(0xff939694),
                focusManager = focusManager
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            CalculatorButton(modifier = Modifier
                .weight(1f),
                onClick = {openDatePickerDialog.value = true},
                bgColor = Color(0xff939694),
                focusManager = focusManager
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
            if(sumText.value.canBeEvaluatedAsMathExpr()) {
                CalculatorButton(
                    modifier = Modifier
                        .weight(2f),
                    onClick = {evaluateSumValue(sumText)},
                    bgColor = Color(0xff479ad6),
                    focusManager = focusManager
                ) {
                    Text(text = "=", fontSize = 22.sp, fontWeight = FontWeight.Bold, color=Color.White)
                }
            }
            else {
                CalculatorButton(
                    modifier = Modifier
                        .weight(2f),
                    onClick = {isSubmitted.value=true},
                    bgColor = Color(0xff479ad6),
                    focusManager = focusManager
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
private fun CalculatorButton(modifier: Modifier = Modifier, onClick: ()->Unit = {}, bgColor: Color = Color(0xffc8ccc9), focusManager: FocusManager, content: @Composable ()-> Unit) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .clip(CircleShape)
            .fillMaxSize()
            .background(bgColor)
            .clickable {
                focusManager.clearFocus()
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun evaluateSumValue(sumText: MutableState<String>) {
    val numbers = sumText.value.split("÷|×|-|\\+".toRegex())
    val secondNumber = if(numbers.size>1 && !numbers[1].isNullOrBlank()) numbers[1].toDouble() else numbers[0].toDouble()
    if(secondNumber!=0.0)
        sumText.value = evaluateSimpleMathExpr(sumText.value).toCalculatorString()
}

private fun String.canBeEvaluatedAsMathExpr(): Boolean {
    if(this.isNullOrEmpty() || !this.contains("÷|×|-|\\+".toRegex())) return false

    if(this.contains("÷|×|-|\\+".toRegex()) && this[0]!='0') return true

    return this.drop(1).contains("÷|×|-|\\+".toRegex())
}


private fun evaluateSimpleMathExpr(mathExprStr: String): Double {
    val n0IsNegative = mathExprStr[0]=='-'

    val numbers = if(n0IsNegative) mathExprStr.drop(1).split("÷|×|-|\\+".toRegex())
    else mathExprStr.split("÷|×|-|\\+".toRegex())

    val n0 = if(n0IsNegative) -numbers[0].toDouble() else numbers[0].toDouble()
    val n1 = if(numbers.size>1 && !numbers[1].isNullOrBlank()) numbers[1].toDouble() else n0

    return if(mathExprStr.contains('÷'))
        n0/n1
    else if(mathExprStr.contains('×'))
        n0*n1
    else if(mathExprStr.contains('-'))
        n0-n1
    else
        n0+n1
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
                                VectorIcon(height = 40.dp , width = 50.dp, vectorImg = it.accountImg, onClick = {})
                                Column(modifier = Modifier.padding(8.dp, 0.dp)) {
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
                                VectorIcon(height = 40.dp , width = 40.dp, vectorImg = it.categoryImg, onClick = {}, cornerSize = 50.dp)
                                Text(modifier = Modifier.padding(8.dp, 0.dp), text = it.title, fontSize = 16.sp)
                            }
                            Divider(modifier = Modifier.background(dividerColor))
                        }
                    }
                )
            }
        }
    }
}