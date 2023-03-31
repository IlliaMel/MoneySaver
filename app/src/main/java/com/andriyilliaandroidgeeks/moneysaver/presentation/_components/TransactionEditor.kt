package com.andriyilliaandroidgeeks.moneysaver.presentation._components

import java.util.*
import android.widget.Toast
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.andriyilliaandroidgeeks.moneysaver.MoneySaver
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData.accountBgImg
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.CategoriesData
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction
import com.andriyilliaandroidgeeks.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.*
import kotlin.math.min

@Composable
fun TransactionEditor(
    currentTransaction: MutableState<Transaction?> = mutableStateOf(null),
    category:  MutableState<Category>,
    addTransaction: (tr: Transaction)->Unit,
    deleteTransaction: (tr: Transaction)->Unit = {},
    closeAdder: ()->Unit,
    accountsList: List<Account>,
    categoriesList: List<Category>,
    minDate: MutableState<Date?>,
    maxDate: MutableState<Date?>,
    editBySwipeWasActivated: MutableState<Boolean> = mutableStateOf(false),
    returnCurrencyValue: (which : String , to : String) -> Double
) {
    val choiceIsActive = remember { mutableStateOf(currentTransaction.value!=null && !editBySwipeWasActivated.value)}
    val sumText = remember { mutableStateOf(
        currentTransaction.value?.sum?.times(
            if(category.value.isForSpendings) -1 else 1
        )?.toCalculatorString() ?: "0")
    }
    val date: MutableState<Date?> = remember { mutableStateOf(currentTransaction.value?.date ?: defaultDateInRange(minDate, maxDate)) }
    var note by remember { mutableStateOf(currentTransaction.value?.note ?: "") }
    val transactionAccount = remember { mutableStateOf(if(currentTransaction.value!=null) accountsList.first{it.uuid == currentTransaction.value!!.accountUUID} else if(accountsList.isNotEmpty()) accountsList[0] else AccountsData.accountsList[0]) }
    val isSubmitted = remember { mutableStateOf(false) }
    val openPickDateDialog = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val selectedCurrencyType = remember { mutableStateOf(transactionAccount.value.currencyType)}
    editBySwipeWasActivated.value = false

    val openChoseAccountDialog = remember { mutableStateOf(false) }
    val openChoseCategoryDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth().background(backgroundSecondaryColor)
            .height(if(choiceIsActive.value) 300.dp else 520.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min)
                ){
                    Image(
                        painter = painterResource(AccountsData.cardBgImg),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        colorFilter = ColorFilter.tint(color = transactionAccount.value.accountImg.externalColor, blendMode = BlendMode.Softlight)
                    )
                    Column(
                        modifier = Modifier
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
                                val saturation = transactionAccount.value.accountImg.externalColor.getSaturation()
                                val color = if (saturation>0.5f) Color.White else Color.Black
                                Text(text = stringResource(R.string.from_account), fontSize = 14.sp, color=color)
                                Text(text = transactionAccount.value.title, fontSize = 17.sp, color=color, overflow = TextOverflow.Ellipsis)
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
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min)
                ){
                    Image(
                        painter = painterResource(AccountsData.cardBgImg),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        colorFilter = ColorFilter.tint(color = category.value.categoryImg.externalColor, blendMode = BlendMode.Softlight)
                    )
                    Column(
                        modifier = Modifier
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
                                val saturation = category.value.categoryImg.externalColor.getSaturation()
                                val color = if (saturation>0.5f) Color.White else Color.Black
                                Text(text = stringResource(R.string.to_category), fontSize = 14.sp, color=color)
                                Text(text = category.value.title, fontSize = 17.sp, color=color, overflow = TextOverflow.Ellipsis)
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
            }


        }
        Divider(modifier = Modifier.background(bordersSecondaryColor))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(4.dp),text = if(category.value.isForSpendings) stringResource(R.string.expense) else stringResource(
                            R.string.income), color = textPrimaryColor, fontSize = 16.sp)
            Text(modifier = Modifier.padding(2.dp),text =  sumText.value + " " + selectedCurrencyType.value.currency, color = category.value.categoryImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Divider(modifier = Modifier.background(bordersSecondaryColor))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                enabled = !choiceIsActive.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "${stringResource(R.string.notes)}...", color = textPrimaryColor, textAlign = TextAlign.Center) },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = textPrimaryColor
                ),
                modifier = Modifier
                    .width(220.dp)
                    .height(55.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                maxLines = 1
            )
        }
        Divider(modifier = Modifier.background(bordersSecondaryColor))

        if(choiceIsActive.value) {
            Row(modifier = Modifier
                .padding(6.dp, 0.dp)
                .fillMaxWidth()
                .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(50.dp)
                            .height(50.dp)
                            .background(Color(0x33E31D0B))
                            .clickable {
                                if(currentTransaction.value!=null)
                                    deleteTransaction(currentTransaction.value!!)
                                closeAdder()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(modifier = Modifier.width(25.dp).height(25.dp), imageVector = Icons.Filled.Delete, contentDescription = null, tint = Color(0xffe31d0b))
                    }
                    Text(
                        modifier = Modifier.padding(0.dp, 4.dp),
                        text = stringResource(R.string.delete),
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = textPrimaryColor,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .width(50.dp)
                        .height(50.dp)
                        .background(Color(0x330B53E3))
                        .clickable {
                            choiceIsActive.value = false
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(modifier = Modifier.width(25.dp).height(25.dp), imageVector = Icons.Filled.Edit, contentDescription = null, tint = Color(0xff0b53e3))
                    }
                    Text(
                        modifier = Modifier.padding(0.dp, 4.dp),
                        text = stringResource(R.string.edit),
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = textPrimaryColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        } else {
            Calculator(
                vectorImg = category.value.categoryImg,
                sumText = sumText,
                isSubmitted = isSubmitted,
                openDatePickerDialog = openPickDateDialog,
                focusManager = focusManager,
                selectedCurrencyType = selectedCurrencyType,
                targetCurrencyType = transactionAccount.value.currencyType,
                returnCurrencyValue = returnCurrencyValue
            )
        }

        Row(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(backgroundSecondaryColor)
                .border(BorderStroke(1.dp, bordersPrimaryColor)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = getShortDateString(date.value?:Date(), LocalContext.current), color = Color(0xff54514d), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

    }

    if(isSubmitted.value) {
        val transactionNote: String? = if (note != "") note else null
        isSubmitted.value = false
        var writtenSum = (sumText.value.toDoubleOrNull() ?: 0.0)
        if(category.value.isForSpendings && writtenSum > (transactionAccount.value.balance + transactionAccount.value.creditLimit))
            Toast
                .makeText(
                    MoneySaver.applicationContext(),
                    MoneySaver
                        .applicationContext()
                        .getString(R.string.lack_of_balance),
                    Toast.LENGTH_SHORT
                )
                .show()
        else {

            val transaction = if (currentTransaction.value == null) Transaction(
                sum = (sumText.value.toDoubleOrNull()
                    ?: 0.0) * if (category.value.isForSpendings) -1 else 1,
                categoryUUID = category.value.uuid,
                accountUUID = transactionAccount.value.uuid,
                date = date.value ?: Date(),
                note = transactionNote
            ) else Transaction(
                uuid = currentTransaction.value!!.uuid,
                sum = (sumText.value.toDoubleOrNull()
                    ?: 0.0) * if (category.value.isForSpendings) -1 else 1,
                categoryUUID = category.value.uuid,
                accountUUID = transactionAccount.value.uuid,
                date = date.value ?: Date(),
                note = transactionNote
            )
            addTransaction(transaction)
            sumText.value = "0"
            closeAdder()
        }
    }

    DatePickerDialog(openDialog = openPickDateDialog, startDate = date )
    ChooseTransactionAccountDialog(openDialog = openChoseAccountDialog, accountList = accountsList, transactionAccount = transactionAccount, selectedCurrencyType = selectedCurrencyType)
    ChooseTransactionCategoryDialog(openDialog = openChoseCategoryDialog, categoryList = categoriesList, transactionCategory = category)
}

private fun defaultDateInRange(minDate: MutableState<Date?>, maxDate: MutableState<Date?>): Date {
    val currentDate = Date()
    if(minDate.value==null || maxDate.value==null) return currentDate
    if(minDate.value!!<currentDate && maxDate.value!!>currentDate) return currentDate
    if(minDate.value!!>currentDate) return minDate.value!!
    return maxDate.value!!
}

/**
 * @return value between 0.0 and 1.0
 */
fun Color.getSaturation(): Float {
    val minValue = min(min(this.red, this.green), this.blue)
    return 1f - (minValue/1f)
}


@Composable
fun ChooseTransactionAccountDialog(openDialog: MutableState<Boolean>, accountList: List<Account>, transactionAccount: MutableState<Account>, selectedCurrencyType: MutableState<Currency>? = null) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .width(230.dp)
                    .height(220.dp)
                    .background(backgroundSecondaryColor)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
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
                                        selectedCurrencyType?.let {
                                            selectedCurrencyType.value = transactionAccount.value.currencyType
                                        }
                                        openDialog.value = false
                                    }
                                    .background(if (it == transactionAccount.value) Color(0xFF585E5F) else backgroundSecondaryColor)
                                    .padding(2.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    VectorIcon(
                                        modifier = Modifier
                                            .padding(8.dp),
                                        height = 35.dp,
                                        width = 48.dp,
                                        vectorImg = it.accountImg,
                                        onClick = {
                                            transactionAccount.value = it
                                            openDialog.value = false
                                        })
                                }
                                Column() {
                                    Text(it.title,color = textPrimaryColor, fontSize = 14.sp)
                                    val balanceText: String = (if(it.balance>0) "+" else (if(it.balance < 0) "-" else ""))+"${it.currencyType.currency} "+it.balance
                                    val balanceColor = if(it.balance>0) currencyColor else (if(it.balance < 0) currencyColorSpent else currencyColorZero)
                                    Text(text = balanceText, color = balanceColor, fontSize = 12.sp)
                                }
                            }
                            Divider(modifier = Modifier.background(bordersSecondaryColor))
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
                    .width(230.dp)
                    .height(320.dp)
                    .background(backgroundSecondaryColor)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
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
                                            0xFF585E5F
                                        ) else backgroundSecondaryColor
                                    )
                                    .padding(10.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VectorIcon(modifierIcn = Modifier.padding(6.dp), height = 40.dp , width = 40.dp, vectorImg = it.categoryImg, onClick = {
                                    transactionCategory.value = it
                                    openDialog.value = false}, cornerSize = 50.dp)
                                Text(modifier = Modifier.padding(8.dp, 0.dp), color = textPrimaryColor ,text = it.title, fontSize = 16.sp)
                            }
                            Divider(modifier = Modifier.background(bordersSecondaryColor))
                        }
                    }
                )
            }
        }
    }
}