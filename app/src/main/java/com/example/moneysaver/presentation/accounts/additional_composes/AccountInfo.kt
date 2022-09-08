package com.example.moneysaver.presentation.accounts.additional_composes

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.presentation.MainActivityViewModel
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation.accounts.AccountsViewModel
import com.example.moneysaver.ui.theme.whiteSurface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AccountInfo (viewModel: AccountsViewModel = hiltViewModel(),
                 mainActivityViewModel: MainActivityViewModel = hiltViewModel(),
                 isForEditing: MutableState<Boolean>,
                 selectedAccountIndex: MutableState<Int>,
                 collapseBottomSheet: () -> Unit,

                 transactionToAccount: MutableState<Account>,
                 transactionAccount: MutableState<Account>,
                 lookingInfo: MutableState<Boolean>,
                 sumText: MutableState<String>)
{


    var note by remember { mutableStateOf( "") }
    val openPickDateDialog = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val isSubmitted = remember { mutableStateOf(false) }
    val selectedCurrencyType = remember { mutableStateOf(transactionAccount.value.currencyType)}

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
                    sum = writtenSum,
                    categoryUUID = null,
                    accountUUID = transactionAccount.value.uuid,
                    toAccountUUID = transactionToAccount.value.uuid,
                    date = Date(),
                    note = transactionNote
                )
             //   viewModel.addAccount(transactionAccount.value)
              //  viewModel.addAccount(transactionToAccount.value)
                mainActivityViewModel.addTransaction(transaction)
                isSubmitted.value=false
                collapseBottomSheet()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteSurface)
            .height(if (lookingInfo.value) 300.dp else 420.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(!lookingInfo.value){
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    AccountInfoItem(modifier = Modifier.weight(1f),lookingInfo = lookingInfo.value, isAccountTransactionTo = false, openChoseAccountDialog = openChoseAccountDialog , transactionAccount = transactionAccount)
                    AccountInfoItem(modifier = Modifier.weight(1f),lookingInfo = lookingInfo.value, isAccountTransactionTo = true, openChoseAccountDialog = openChoseToAccountDialog , transactionAccount = transactionToAccount)
                }
                Divider()
            }

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(modifier = Modifier.padding(4.dp),text = stringResource(R.string.transfer), color = Color.Black, fontSize = 16.sp)
                Text(modifier = Modifier.padding(2.dp),text =  sumText.value + " " + selectedCurrencyType.value.currency, color = transactionAccount.value.accountImg.externalColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Divider()

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    enabled = !isSubmitted.value,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "${stringResource(R.string.notes)}...", textAlign = TextAlign.Center) },
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
            Divider()

            Calculator(
                transactionToAccount.value.accountImg,
                sumText, isSubmitted, openPickDateDialog,
                focusManager,
                selectedCurrencyType,
                transactionAccount.value.currencyType,
                viewModel::returnCurrencyValue
            )
        }


        if(lookingInfo.value){
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
                            collapseBottomSheet()
                            isForEditing.value = true
                            selectedAccountIndex.value = 1
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


            Column(modifier = Modifier.weight(1f),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(50.dp)
                        .height(50.dp)
                        .background(Color(0x3365E30B))
                        .clickable {
                            if (viewModel.state.allAccountList.size > 2)
                                lookingInfo.value = false
                            else
                                Toast
                                    .makeText(
                                        MoneySaver.applicationContext(),
                                        MoneySaver
                                            .applicationContext()
                                            .getString(R.string.add_at_least_two_accounts),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp),
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0x9065E30B)
                    )
                }
                Text(
                    modifier = Modifier.padding(0.dp, 4.dp),
                    text = stringResource(R.string.transaction),
                    fontSize = 16.sp
                )
            }
        }
        }
    }

    DatePickerDialog(openDialog = openPickDateDialog, startDate = remember {
        mutableStateOf(Date())
    } )
    ChooseTransactionAccountDialog(openDialog = openChoseAccountDialog, accountList = viewModel.state.allAccountList.filter { !(it.isForDebt && it.isForGoal) }, transactionAccount = transactionAccount)
    ChooseTransactionAccountDialog(openDialog = openChoseToAccountDialog, accountList = viewModel.state.allAccountList.filter { !(it.isForDebt && it.isForGoal) }, transactionAccount = transactionToAccount)

}


@Composable
fun AccountInfoItem(modifier : Modifier = Modifier,lookingInfo : Boolean , isAccountTransactionTo : Boolean, openChoseAccountDialog: MutableState<Boolean> , transactionAccount: MutableState<Account>){

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ){
        Image(
            painter = painterResource(R.drawable.bg5),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            colorFilter = ColorFilter.tint(color = transactionAccount.value.accountImg.externalColor, blendMode = BlendMode.Softlight)
        )
        Column(
            modifier = Modifier
                .clickable { if (!lookingInfo) openChoseAccountDialog.value = true }
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
                    Text(text = if(isAccountTransactionTo) stringResource(R.string.to_account) else  stringResource( R.string.from_account), fontSize = 14.sp, color=color)
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
}