package com.example.moneysaver.presentation._components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.CategoriesData
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
    var sumText = remember { mutableStateOf(currentTransaction?.sum?.times(-1)?.toCalculatorString() ?: "0") }
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
                            Text(text = stringResource(R.string.from_account), fontSize = 14.sp, color=Color.White)
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
                            Text(text = stringResource(R.string.to_category), fontSize = 14.sp, color=Color.White)
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
            Text(modifier = Modifier.padding(2.dp),text = stringResource(R.string.expense), color = category.value.categoryImg.externalColor, fontSize = 16.sp)
            Text(modifier = Modifier.padding(2.dp),text = "$ "+ sumText.value, color = category.value.categoryImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                enabled = !choiceIsActive.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "${stringResource(R.string.notes)}...", textAlign = TextAlign.Center) },
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
                    Text(text = stringResource(R.string.delete), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = stringResource(R.string.edit), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
            sum = (sumText.value.toDoubleOrNull() ?: 0.0) * -1,
            categoryUUID = category.value.uuid,
            accountUUID = transactionAccount.value.uuid,
            date = date.value?:Date(),
            note = transactionNote
        ) else Transaction(
            uuid=currentTransaction.uuid,
            sum = (sumText.value.toDoubleOrNull() ?: 0.0) * -1,
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
                    items = categoryList.filter { it.uuid!=CategoriesData.addCategory.uuid },
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