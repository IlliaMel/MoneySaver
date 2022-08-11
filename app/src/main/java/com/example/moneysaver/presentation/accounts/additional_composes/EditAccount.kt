package com.example.moneysaver.presentation.accounts.additional_composes
import android.icu.text.CaseMap
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.ui.theme.*

@Composable
fun EditAccount(
    isEditing: Boolean,
    account: Account = AccountsData.accountsList.get(0),
    onAddAccountAction: (Account) -> Unit,
    onDeleteAccount: (Account) -> Unit,
    onCancelIconClick: () -> Unit
){
/*
    var accountType = remember { mutableStateOf(account.type) }
    var setSelectedAccount = remember { mutableStateOf(false) }
    var setSelectedAccount = remember { mutableStateOf(false) }
    */


    var setCurrencyTypeChange = remember { mutableStateOf(false) }
    var setDescriptionChange = remember { mutableStateOf(false) }
    var setBalance = remember { mutableStateOf(false) }
    var setCreditLimit = remember { mutableStateOf(false) }


    var currencyType =  remember { mutableStateOf(account.currencyType) }
    var description =  remember { mutableStateOf(account.description) }
    var title =  remember { mutableStateOf(account.title) }


    var amountOfDept =  remember { mutableStateOf(account.debt) }
    var amountOfBalance =  remember { mutableStateOf(account.balance) }
    var amountOfCreditLimit =  remember { mutableStateOf(account.creditLimit) }



    /*
    0 - simple
    1 - debt
    2 - goal

     */


    var type =
        if(account.isForDebt)
            1
        else if (account.isForGoal)
            2
        else
            0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(gray)
    ) {
/*

Account(description = description.value,
                    currencyType = currencyType.value,
                    title = it)
 */

        TopBarAccounts(title = title.value, typeOfAccount = if(isEditing)  "Edit Account" else  "New Account",
            onAddAccountAction = {onAddAccountAction(
                if(isEditing){
                    if(type == 1) {
                        Account(
                            uuid = account.uuid,
                            debt = amountOfDept.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it
                        )
                    }else if(type == 0) {
                        Account(
                            uuid = account.uuid,
                            creditLimit = amountOfCreditLimit.value,
                            balance = amountOfBalance.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it
                        )
                    }else {
                        Account(
                            uuid = account.uuid,
                            balance = amountOfBalance.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it
                        )
                    }
                }else{
                    if(type == 0){
                        Account(
                            creditLimit = amountOfCreditLimit.value,
                            balance = amountOfBalance.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it)
                    }else if (type == 1){
                        Account(
                            debt = amountOfDept.value,
                            isForDebt = true,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it)
                    }else{
                        Account(
                            balance = amountOfBalance.value,
                            isForGoal = true,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it)
                    }

                }

            )}, onCancelIconClick = {onCancelIconClick()})
       // dividerForTopBar()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteSurface),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {


            Column(modifier = Modifier.shadow(elevation = 1.dp)) {

                Text(
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),
                    text = "Account",
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )

                accountEditInfoText(
                    upperText = "Type",
                    bottomText = whichTypeOfAccount(account),
                    onAction = {},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
                accountEditInfoText(
                    upperText = "Currency Type",
                    bottomText = currencyType.value,
                    onAction = {setCurrencyTypeChange.value = true},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
                accountEditInfoText(
                    upperText = "Description",
                    bottomText = description.value,
                    onAction = {setDescriptionChange.value = true},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
            }

            Box(modifier = Modifier
                .background(gray)
                .height(15.dp)
                .fillMaxWidth())

            Column(modifier = Modifier.shadow(elevation = 1.dp)){

                Text(
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),
                    text = "Balance",
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )

                var moneyValue: String = ""
                when(type){
                    0-> moneyValue = "Balance"

                    1-> moneyValue = "Amount Of Debt"

                    2-> moneyValue = "Main Goal"
                }

                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .clickable {setBalance.value = true},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                        text = moneyValue,
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )

                    Text(
                        modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
                        text = if(account.isForDebt) amountOfDept.value.toString() else amountOfBalance.value.toString(),
                        color = whichColorOfAccount(account , if(account.isForDebt) amountOfDept.value else amountOfBalance.value),
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )

                }
                Divider()

                if(type == 0) {
                    Row(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                            .fillMaxWidth()
                            .clickable {setCreditLimit.value = true},
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                            text = "Credit Limit",
                            color = Color.Black,
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp
                        )

                        Text(
                            modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
                            text = amountOfCreditLimit.value.toString(),
                            color = currencyColor,
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp
                        )
                    }
                }
                Divider()
               /*
                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .clickable {},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                        text = "Add to whole Balance amount",
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )
                    val checkedState = remember { mutableStateOf(true) }
                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it }
                    )

                }
                */
            }

            Box(modifier = Modifier
                .background(gray)
                .height(20.dp)
                .fillMaxWidth())

            Row(modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .clickable { onDeleteAccount(account) }
                .shadow(elevation = 1.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
                Icon(modifier = Modifier
                    .padding(16.dp, 16.dp, 0.dp, 16.dp),
                    imageVector = Icons.Default.Delete,
                    tint = Color.Red,
                    contentDescription = "Delete button"
                )

                Text(
                    modifier = Modifier.padding(24.dp, 16.dp, 0.dp, 16.dp),
                    text = "Delete Account",
                    color = Color.Red,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp
                )


            }


        }

    }

    SetAccountBalance(openDialog = setCreditLimit, returnType = {setCreditLimit.value = false; amountOfCreditLimit.value = it.toDouble() })
    SetAccountBalance(openDialog = setBalance, returnType = {setBalance.value = false; if(account.isForDebt) amountOfDept.value = it.toDouble() else amountOfBalance.value = it.toDouble() })
    SetAccountDescription(description = account.description,openDialog = setDescriptionChange, returnType = {setDescriptionChange.value = false; description.value = it })
    SetAccountCurrencyType(openDialog = setCurrencyTypeChange, returnType = {setCurrencyTypeChange.value = false; currencyType.value = it })

}

fun whichTypeOfAccount(account: Account) : String{
    return if(account.isForDebt)
        "Dept"
    else if (account.isForGoal)
        "Goal"
    else
        "Simple"
}

fun whichColorOfAccount(account: Account, value : Double) : Color{
    return if(account.isForDebt)
         currencyColorSpent
    else if (value == 0.0)
        currencyColorZero
    else
        currencyColor
}
@Composable
fun accountEditInfoText(
    startPadding : Dp = 8.dp,
    topPadding : Dp = 2.dp,
    endPadding : Dp = 0.dp,
    bottomPadding : Dp = 4.dp,
    upperText: String,
    bottomText: String,
    onAction: () -> Unit
){

    Column (modifier = Modifier
        .fillMaxWidth()
        .clickable { onAction() },
    verticalArrangement = Arrangement.SpaceEvenly,
    horizontalAlignment = Alignment.Start){
            Text(
                modifier = Modifier.padding(startPadding, topPadding,endPadding, 0.dp),
                text = upperText,
                color = Color.Black,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.padding(startPadding, topPadding/4,endPadding, bottomPadding),
                text = bottomText,
                color = currencyColorZero,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp
            )
        Divider()
    }


}

@Composable
fun SetAccountBalance(
    openDialog: MutableState<Boolean>,
    returnType: (type: String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("0.0") }
    val focusManager = LocalFocusManager.current
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {

            Column(modifier = Modifier
                .defaultMinSize()
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(
                    whiteSurface
                ),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally){
                Row(
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(modifier = Modifier.padding(32.dp,0.dp,0.dp,0.dp), text = "Amount Of Money", color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 20.sp)
                }

                TextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.W400 , fontSize = 18.sp),
                    value = text,
                    onValueChange = {
                        text = if (it.isEmpty()) {
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> text //old value
                                else -> it   //new value
                            }
                        }
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                )

                ButtonWithColor(returnType = {returnType(text)}, text)

            }




        }
    }

}

@Composable
fun SetAccountDescription(
    description: String,
    openDialog: MutableState<Boolean>,
    returnType: (type: String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf(description) }
    val focusManager = LocalFocusManager.current
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {

            Column(modifier = Modifier
                .defaultMinSize()
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(
                    whiteSurface
                ),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally){
                Row(
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(modifier = Modifier.padding(32.dp,0.dp,0.dp,0.dp), text = "Account Description", color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 20.sp)
                }

                TextField(
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.W400 , fontSize = 18.sp),
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                )

                ButtonWithColor(returnType = {returnType(text)}, text)

            }




        }
    }

}

@Composable
fun ButtonWithColor(returnType: (type: String) -> Unit,  text : String){
    Button(modifier = Modifier.padding(0.dp,4.dp,0.dp,8.dp),onClick = {
        returnType(text)
    },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray))

    {
        Text(text = "  OK  ",color = Color.White)
    }
}

@Composable
fun SetAccountCurrencyType(
    openDialog: MutableState<Boolean>,
    returnType: (type: String) -> Unit,
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {

            Column(modifier = Modifier
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(
                    whiteSurface
                )) {
                Row(
                    modifier = Modifier
                        .padding(0.dp, 4.dp, 0.dp, 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(modifier = Modifier.padding(22.dp,4.dp,0.dp,4.dp), text = "Choose Currency Type", color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 20.sp)

                }

                LazyColumn(
                    modifier = Modifier
                        .padding(4.dp,8.dp,4.dp,8.dp),
                    verticalArrangement = Arrangement.Center,
                    contentPadding = PaddingValues()
                ) {
                    items(
                        items = AccountsData.currencyTypes,
                        itemContent = {
                            Row(
                                modifier = Modifier
                                    .padding(0.dp, 4.dp, 0.dp, 4.dp)
                                    .fillMaxWidth()
                                    .clickable { returnType(it.currency) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(modifier = Modifier.padding(22.dp,4.dp,0.dp,4.dp), text = it.description, color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 18.sp)
                                Text(modifier = Modifier.padding(0.dp,4.dp,32.dp,4.dp), text = it.currency, color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 18.sp)
                            }
                        }
                    )

                }
            }




        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarAccounts(
    title: String,
    typeOfAccount: String,
    onAddAccountAction: (String) -> Unit,
    onCancelIconClick: () -> Unit){
    var text by rememberSaveable { mutableStateOf(title) }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = painterResource(R.drawable.bg5),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            Column(modifier = Modifier
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Row(modifier = Modifier
                    .padding(0.dp, 48.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top){
                    IconButton(modifier = Modifier
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .size(40.dp, 40.dp)
                        .weight(1f), onClick = {onCancelIconClick()}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = whiteSurface,
                            contentDescription = "Close button"
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(4.5f)
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween) {

                        Text(modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp) ,text = typeOfAccount, color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 22.sp)

                        Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                        Text(modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),text = "Name", color = whiteSurfaceTransparent, fontWeight = FontWeight.W400 , fontSize = 16.sp)
                        TextField(
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                                textStyle = TextStyle(color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 24.sp),
                                value = text,
                                onValueChange = {
                                    text = it
                                },
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                textColor = whiteSurface,
                                disabledTextColor = Color.Transparent,
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = whiteSurface,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            )
                        }
                    }



                    IconButton(modifier = Modifier
                        .padding(0.dp, 0.dp, 8.dp, 0.dp)
                        .size(40.dp, 40.dp)
                        .weight(1f), onClick = { onAddAccountAction(text) }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            tint = whiteSurface,
                            contentDescription = "Apply button"
                        )
                    }

                }

           }

        }
    }
}