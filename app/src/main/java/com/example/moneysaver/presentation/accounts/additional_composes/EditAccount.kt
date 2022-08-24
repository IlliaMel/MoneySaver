package com.example.moneysaver.presentation.accounts.additional_composes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.CategoriesData
import com.example.moneysaver.data.data_base._test_data.VectorImg
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.ui.theme.*
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun EditAccount(
    isEditing: Boolean,
    account: Account = AccountsData.accountsList.get(0),
    onAddAccountAction: (Account) -> Unit,
    onDeleteAccount: (Account) -> Unit,
    onCancelIconClick: () -> Unit
){


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
    var img =  remember { mutableStateOf(account.accountImg) }


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


        TopBarAccounts(vectorImg =  img.value ,title = title.value,onChangeImg = {img.value = it}, typeOfAccount = if(isEditing)  stringResource(
                    R.string.edit_account) else  stringResource(R.string.new_account),
            onAddAccountAction = {onAddAccountAction(
                if(isEditing){
                    if(type == 1) {
                        Account(
                            uuid = account.uuid,
                            accountImg = img.value,
                            debt = amountOfDept.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            isForDebt = true,
                            isForGoal = false,
                            title = it
                        )
                    }else if(type == 0) {
                        Account(
                            uuid = account.uuid,
                            accountImg = img.value,
                            creditLimit = amountOfCreditLimit.value,
                            balance = amountOfBalance.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            isForDebt = false,
                            isForGoal = false,
                            title = it
                        )
                    }else {
                        Account(
                            uuid = account.uuid,
                            accountImg = img.value,
                            balance = amountOfBalance.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            isForDebt = false,
                            isForGoal = true,
                            title = it
                        )
                    }
                }else{
                    if(type == 0){
                        Account(
                            accountImg = img.value,
                            creditLimit = amountOfCreditLimit.value,
                            balance = amountOfBalance.value,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it)
                    }else if (type == 1){
                        Account(
                            debt = amountOfDept.value,
                            accountImg = img.value,
                            isForDebt = true,
                            description = description.value,
                            currencyType = currencyType.value,
                            title = it)
                    }else{
                        Account(
                            balance = amountOfBalance.value,
                            accountImg = img.value,
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
                    text = stringResource(R.string.account),
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )

                accountEditInfoText(
                    upperText = stringResource(R.string.type),
                    bottomText = whichTypeOfAccount(account),
                    onAction = {},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
                accountEditInfoText(
                    upperText = stringResource(R.string.currency_type),
                    bottomText = currencyType.value,
                    onAction = {setCurrencyTypeChange.value = true},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
                accountEditInfoText(
                    upperText = stringResource(R.string.description),
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
                    0-> moneyValue = stringResource(R.string.balance)

                    1-> moneyValue = stringResource(R.string.amount_of_debt)

                    2-> moneyValue = stringResource(R.string.main_goal)
                }

                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .clickable { setBalance.value = true },
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
                            .clickable { setCreditLimit.value = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                            text = stringResource(R.string.credit_limit),
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
                    contentDescription = stringResource(R.string.delete_button)
                )

                Text(
                    modifier = Modifier.padding(24.dp, 16.dp, 0.dp, 16.dp),
                    text = stringResource(R.string.delete_account),
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
                    Text(modifier = Modifier.padding(32.dp,0.dp,0.dp,0.dp), text = stringResource(R.string.account_description), color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 20.sp)
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
        Text(text = "  ${stringResource(R.string.ok)}  ",color = Color.White)
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
                    Text(modifier = Modifier.padding(22.dp,4.dp,0.dp,4.dp), text = stringResource(R.string.choose_currency_type), color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 20.sp)

                }

                LazyColumn(
                    modifier = Modifier
                        .padding(4.dp,8.dp,4.dp,8.dp),
                    verticalArrangement = Arrangement.Center,
                    contentPadding = PaddingValues()
                ) {
                    items(
                        items = AccountsData.currenciesList,
                        itemContent = {
                            Row(
                                modifier = Modifier
                                    .padding(0.dp, 4.dp, 0.dp, 4.dp)
                                    .fillMaxWidth()
                                    .clickable { returnType(it.currency) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(modifier = Modifier.padding(22.dp,4.dp,0.dp,4.dp), text = it.currencyName, color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 18.sp)
                                Text(modifier = Modifier.padding(0.dp,4.dp,32.dp,4.dp), text = if(it.valueInMainCurrency.toString().length > 6) it.valueInMainCurrency.toString() else it.valueInMainCurrency.toString() , color = Color.Black, fontWeight = FontWeight.W500 , fontSize = 18.sp)
                            }
                        }
                    )

                }
            }




        }
    }

}

@Composable
fun TopBarAccounts(
    title: String,
    typeOfAccount: String,
    vectorImg: VectorImg,
    onChangeImg: (VectorImg) -> Unit,
    onAddAccountAction: (String) -> Unit,
    onCancelIconClick: () -> Unit){
    var text by rememberSaveable { mutableStateOf(title) }
    val focusManager = LocalFocusManager.current

    var setAccountImg = remember { mutableStateOf(false) }

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
                        .weight(1.2f), onClick = {onCancelIconClick()}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = whiteSurface,
                            contentDescription = stringResource(R.string.close_button)
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(3.8f)
                        .padding(0.dp, 0.dp, 16.dp, 0.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween) {

                        Text(modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp) ,text = typeOfAccount, color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 22.sp)

                        Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                        Text(modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),text = stringResource(R.string.name), color = whiteSurfaceTransparent, fontWeight = FontWeight.W400 , fontSize = 16.sp)
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


                    Column(modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .padding(0.dp, 0.dp, 13.dp, 0.dp),verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally){
                        IconButton(modifier = Modifier
                            .size(35.dp, 35.dp),
                            onClick = { onAddAccountAction(text) }) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                tint = whiteSurface,
                                contentDescription = stringResource(R.string.apply_button)
                            )
                        }

                        VectorIcon(modifier =  Modifier.padding(0.dp, 0.dp, 0.dp, 18.dp),vectorImg = vectorImg , onClick = {setAccountImg.value = true})

                    }

                }

           }

        }
    }
    SetImg(openDialog = setAccountImg, returnType={setAccountImg.value = false; onChangeImg(it) ; } , listOfVectors = AccountsData.accountImges, idForCategory = false)
}

@Composable
fun VectorIcon(modifier: Modifier = Modifier, modifierIcn: Modifier = Modifier, vectorImg : VectorImg, onClick: () -> Unit, width : Dp = 55.dp,  height : Dp = 45.dp, cornerSize : Dp  = 8.dp){
    Box(modifier = modifier
        .padding(0.dp, 0.dp, 0.dp, 0.dp)
        .width(width)
        .height(height)
        .clickable { onClick() }
        .clip(RoundedCornerShape(corner = CornerSize(cornerSize)))
        .background(vectorImg.externalColor),
        contentAlignment = Alignment.Center){
        Icon(modifier = modifierIcn,
            imageVector = ImageVector.vectorResource(vectorImg.img),
            tint = vectorImg.innerColor,
            contentDescription = null
        )
    }
}

@Composable
fun SetImg(
    openDialog: MutableState<Boolean>,
    returnType: (img: VectorImg) -> Unit,
    listOfVectors: MutableList<VectorImg>,
    chosenVectorImg: VectorImg = VectorImg(),
    idForCategory: Boolean
) {


    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            var tabIndex by remember { mutableStateOf(0) }
            val tabData = listOf(
                stringResource(R.string.image),
                stringResource(R.string.color),
            )
            val controller = rememberColorPickerController()
            var chosenVectorImg = remember { mutableStateOf(chosenVectorImg) }


            if(tabIndex == 0){
                chosenVectorImg.value.externalColor = externalColorGray
                Column(modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                    .background(
                        whiteSurface
                    )) {

                    TabRow(selectedTabIndex = tabIndex,
                           backgroundColor = Color.White,
                           contentColor = Color.White,
                        modifier = Modifier.shadow(elevation = 5.dp)
                    ) {
                        tabData.forEachIndexed { index, text ->
                            Tab(selected = tabIndex == index,
                                onClick = {
                                tabIndex = index
                            }){
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (tabIndex == index) inactiveColorTab else Color.White), contentAlignment = Alignment.Center) {
                                    Text(
                                        modifier = Modifier
                                            .padding(12.dp),
                                        text = text,
                                        fontSize = 16.sp,
                                        color = if (tabIndex == index) Color.Black else inactiveColor
                                    )
                                }
                            }
                        }
                    }


                    LazyVerticalGrid(
                        modifier = Modifier.padding(0.dp,0.dp,0.dp,16.dp),
                        columns = GridCells.Fixed(3),
                        // content padding
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            top = 16.dp,
                            end = 12.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalArrangement = Arrangement.SpaceAround,
                        content = {
                            items(listOfVectors.size) { index ->

                                if(idForCategory){
                                    VectorIcon(Modifier.padding(8.dp,8.dp,8.dp,8.dp),Modifier.padding(12.dp), vectorImg = CategoriesData.categoryImges[index], onClick = {tabIndex = 1 ; chosenVectorImg.value = VectorImg(img = listOfVectors[index].img)},width = 50.dp , height = 60.dp, cornerSize = 50.dp)
                                }else{
                                    Card(
                                        backgroundColor = listOfVectors[index].externalColor,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .width(45.dp)
                                            .height(45.dp),
                                        elevation = 8.dp,
                                    ) {
                                        VectorIcon(Modifier.padding(8.dp,0.dp,8.dp,0.dp),vectorImg = listOfVectors[index], onClick = {tabIndex = 1 ; chosenVectorImg.value = VectorImg(img = listOfVectors[index].img)})
                                    }
                                }

                            }
                        }
                    )
                }
            }else if (tabIndex == 1){

                Column(modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                    .background(
                        whiteSurface
                    )) {

                    TabRow(selectedTabIndex = tabIndex,
                        backgroundColor = Color.White,
                        contentColor = Color.White,
                        modifier = Modifier.shadow(elevation = 5.dp)
                    ) {
                        tabData.forEachIndexed { index, text ->
                            Tab(selected = tabIndex == index,
                                onClick = {
                                    tabIndex = index
                                }){
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (tabIndex == index) inactiveColorTab else Color.White), contentAlignment = Alignment.Center) {
                                    Text(
                                        modifier = Modifier
                                            .padding(12.dp),
                                        text = text,
                                        fontSize = 16.sp,
                                        color = if (tabIndex == index) Color.Black else externalColorGray
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        HsvColorPicker(
                            modifier = Modifier
                                .weight(2f)
                                .height(250.dp)
                                .padding(8.dp),
                            controller = controller,
                        )



                        if(idForCategory){
                            var vectorImg: VectorImg = chosenVectorImg.value
                            vectorImg.externalColor = if(controller.selectedColor.value == Color.White) externalColorGray else controller.selectedColor.value
                            VectorIcon(
                                Modifier
                                    .weight(1f)
                                    .padding(8.dp, 8.dp, 8.dp, 8.dp),Modifier.padding(8.dp), vectorImg = vectorImg, onClick = {returnType(vectorImg)},width = 30.dp , height = 65.dp, cornerSize = 30.dp)
                        }else{

                            Card(
                                backgroundColor = if(controller.selectedColor.value == Color.White) externalColorGray else controller.selectedColor.value,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                                    .width(72.dp)
                                    .height(45.dp),
                                elevation = 8.dp,
                            ) {
                                var vectorImg: VectorImg = chosenVectorImg.value
                                vectorImg.externalColor = if(controller.selectedColor.value == Color.White) externalColorGray else controller.selectedColor.value
                                VectorIcon(Modifier.padding(0.dp,0.dp,0.dp,0.dp), vectorImg = vectorImg, onClick = { returnType(vectorImg)})
                            }
                        }


                    }
                }
            }
        }
    }
}

