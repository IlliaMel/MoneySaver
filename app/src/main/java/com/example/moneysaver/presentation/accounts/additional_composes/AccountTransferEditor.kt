package com.example.moneysaver.presentation.accounts.additional_composes

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.presentation.MainActivityViewModel
import com.example.moneysaver.presentation._components.Calculator
import com.example.moneysaver.presentation._components.ChooseTransactionAccountDialog
import com.example.moneysaver.presentation._components.DatePickerDialog
import com.example.moneysaver.presentation._components.getShortDateString
import com.example.moneysaver.presentation._components.navigation_drawer.WithdrawalDepositField
import com.example.moneysaver.presentation.accounts.AccountsViewModel
import com.example.moneysaver.ui.theme.calculatorBorderColor
import com.example.moneysaver.ui.theme.whiteSurface
import java.util.*

@Composable
fun AccountTransferEditor (
    viewModel: AccountsViewModel = hiltViewModel(),
    mainActivityViewModel: MainActivityViewModel = hiltViewModel(),

    currentTransaction: MutableState<Transaction?> = mutableStateOf(null),
    collapseBottomSheet: () -> Unit,

    sumText: MutableState<String>,
    sumTextSecond: MutableState<String>,
)
{

    sumText.value = currentTransaction.value?.sum.toString() ?: "0"
    sumTextSecond.value = currentTransaction.value?.toAccountSum.toString() ?: "0"

    var  isForEditingTransactionView by remember {
        mutableStateOf(true)
    }

    var transactionAccount = remember { mutableStateOf(viewModel.state.allAccountList.find {
        it.uuid == (currentTransaction.value?.accountUUID ?: UUID.randomUUID())
    }?: AccountsData.normalAccount) }


    var transactionToAccount = remember { mutableStateOf(viewModel.state.allAccountList.find {
        it.uuid == (currentTransaction.value?.toAccountUUID ?: UUID.randomUUID())
    }?: AccountsData.normalAccount) }

    var note by remember { mutableStateOf( currentTransaction.value?.note ?: "") }

    val openPickDateDialog = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val isSubmitted = remember { mutableStateOf(false) }
    val selectedCurrencyType = remember { mutableStateOf(transactionAccount.value.currencyType) }
    selectedCurrencyType.value = transactionAccount.value.currencyType
    val selectedCurrencyTypeSecond = remember { mutableStateOf(transactionToAccount.value.currencyType) }
    selectedCurrencyTypeSecond.value = transactionToAccount.value.currencyType
    val isFirstFieldSelected = remember { mutableStateOf(true) }
    isFirstFieldSelected.value = true

    val openChoseAccountDialog = remember { mutableStateOf(false) }
    val openChoseToAccountDialog = remember { mutableStateOf(false) }


    if(isSubmitted.value) {
        if(transactionToAccount.value.uuid == transactionAccount.value.uuid)
            Toast
                .makeText(
                    MoneySaver.applicationContext(),
                    MoneySaver
                        .applicationContext()
                        .getString(R.string.same_account_error),
                    Toast.LENGTH_SHORT
                )
                .show()
        else {

            var writtenSum = (sumText.value.toDoubleOrNull() ?: 0.0) //* mainActivityViewModel.returnCurrencyValue(transactionAccount.value.currencyType.currencyName,transactionToAccount.value.currencyType.currencyName)
            if(writtenSum > (transactionAccount.value.balance + transactionAccount.value.creditLimit))
                Toast
                    .makeText(
                        MoneySaver.applicationContext(),
                        MoneySaver
                            .applicationContext()
                            .getString(R.string.lack_of_balance),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            else{

                val transactionNote: String? = if (note != "") note else null
                val transaction = Transaction(
                    uuid = currentTransaction.value?.uuid ?: UUID.randomUUID(),
                    sum = writtenSum*(-1),
                    categoryUUID = null,
                    accountUUID = transactionAccount.value.uuid,
                    toAccountUUID = transactionToAccount.value.uuid,
                    toAccountSum = sumTextSecond.value.toDoubleOrNull() ?: 0.0,
                    date = currentTransaction.value?.date ?: Date(),
                    note = transactionNote
                )
                mainActivityViewModel.addTransaction(transaction)
                isSubmitted.value=false
                collapseBottomSheet()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isForEditingTransactionView) 320.dp else 480.dp)
            .animateContentSize(),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteSurface)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        AccountInfoItem(modifier = Modifier.weight(1f),lookingInfo = isForEditingTransactionView, isAccountTransactionTo = false, openChoseAccountDialog = openChoseAccountDialog , transactionAccount = transactionAccount)
                        AccountInfoItem(modifier = Modifier.weight(1f),lookingInfo = isForEditingTransactionView, isAccountTransactionTo = true, openChoseAccountDialog = openChoseToAccountDialog , transactionAccount = transactionToAccount)
                    }
                    Divider()
                }

//            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(modifier = Modifier.padding(4.dp),text = stringResource(R.string.transfer), color = Color.Black, fontSize = 16.sp)
//                Text(modifier = Modifier.padding(2.dp),text =  sumText.value + " " + selectedCurrencyType.value.currency, color = transactionAccount.value.accountImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
//            }


                WithdrawalDepositField(
                    selectedAccountFirst = transactionAccount,
                    selectedCurrencyTypeFirst = selectedCurrencyType,
                    sumTextFirst = sumText,
                    selectedAccountSecond = transactionToAccount,
                    selectedCurrencyTypeSecond = selectedCurrencyTypeSecond,
                    sumTextSecond = sumTextSecond,
                    isFirstFieldSelected = isFirstFieldSelected
                )


                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        enabled = !isForEditingTransactionView,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "${
                            stringResource(
                                R.string.notes)
                        }...", textAlign = TextAlign.Center) },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .width(220.dp)
                            .height(50.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            disabledBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        maxLines = 1
                    )
                }

                if(!isForEditingTransactionView) {
                    Divider()

                    Calculator(
                        vectorImg = transactionToAccount.value.accountImg,
                        sumText = sumText,
                        sumTextSecond = sumTextSecond,
                        isFirstFieldSelected = isFirstFieldSelected,
                        isSubmitted = isSubmitted,
                        openDatePickerDialog = openPickDateDialog,
                        focusManager = focusManager,
                        selectedCurrencyType = selectedCurrencyType,
                        selectedCurrencyTypeSecond = selectedCurrencyTypeSecond,
                        targetCurrencyType = transactionAccount.value.currencyType,
                        targetCurrencyTypeSecond = transactionToAccount.value.currencyType,
                        returnCurrencyValue = viewModel::returnCurrencyValue
                    )
                }




            if(isForEditingTransactionView){

                Row(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .background(whiteSurface)
                        .border(BorderStroke(1.dp, calculatorBorderColor)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = getShortDateString(currentTransaction.value?.date ?: Date(), LocalContext.current), color = Color(0xff54514d), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Column(modifier = Modifier.weight(1f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .height(50.dp)
                                .background(Color(0x33E31D0B))
                                .clickable {
                                    viewModel.deleteAccount(transactionAccount.value)
                                    collapseBottomSheet()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .width(25.dp)
                                    .height(25.dp),
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                tint = Color(0xffe31d0b)
                            )
                        }
                        Text(
                            modifier = Modifier.padding(0.dp, 4.dp),
                            text = stringResource(R.string.delete),
                            fontSize = 16.sp
                        )
                    }
                        Column(modifier = Modifier.weight(1f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .width(50.dp)
                                    .height(50.dp)
                                    .background(Color(0x330B53E3))
                                    .clickable {
                                        isForEditingTransactionView = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .width(25.dp)
                                        .height(25.dp),
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = null,
                                    tint = Color(0xff0b53e3)
                                )
                            }
                            Text(
                                modifier = Modifier.padding(0.dp, 4.dp),
                                text = stringResource(R.string.edit),
                                fontSize = 16.sp
                            )
                        }
                }
            }

        }

        if(!isForEditingTransactionView)
        Row(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(whiteSurface)
                .border(BorderStroke(1.dp, calculatorBorderColor)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = getShortDateString(currentTransaction.value?.date ?: Date(), LocalContext.current), color = Color(0xff54514d), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }

    DatePickerDialog(openDialog = openPickDateDialog, startDate = remember {
        mutableStateOf(Date())
    } )
    ChooseTransactionAccountDialog(openDialog = openChoseAccountDialog, accountList = viewModel.state.allAccountList.filter { !(it.isForDebt && it.isForGoal) }, transactionAccount = transactionAccount, selectedCurrencyType = selectedCurrencyType)
    ChooseTransactionAccountDialog(openDialog = openChoseToAccountDialog, accountList = viewModel.state.allAccountList.filter { !(it.isForDebt && it.isForGoal) }, transactionAccount = transactionToAccount, selectedCurrencyType = selectedCurrencyTypeSecond)

}