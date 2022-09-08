package com.example.moneysaver.presentation.accounts

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.domain.model.Account

import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import com.example.moneysaver.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.presentation.MainActivity
import com.example.moneysaver.presentation.MainActivityViewModel
import com.example.moneysaver.presentation._components.CategoryChooser
import com.example.moneysaver.presentation._components.TransactionEditor
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.presentation.accounts.additional_composes.*
import com.example.moneysaver.presentation.transactions.showNoAccountOrCategoryMessage
import com.example.moneysaver.ui.theme.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Accounts(
    onNavigationIconClick: () -> Unit,
    onChosenAccountFilterClick: (Account) -> Unit,
    chosenAccountFilter: Account,
    viewModel: AccountsViewModel = hiltViewModel(),
    baseCurrency: Currency,
) {


    var chosenAccount  = remember { mutableStateOf(AccountsData.accountsList.get(0)) }

    var transactionAccount = remember { mutableStateOf(AccountsData.accountsList.get(0)) }
    var transactionToAccount = remember { mutableStateOf(AccountsData.accountsList.get(0)) }

    var setSelectedAccount = remember { mutableStateOf(false) }
    var isSelectedFilterAccount = remember { mutableStateOf(false) }

    var selectedAccountIndex = remember { mutableStateOf(0) }
    var isForEditing = remember { mutableStateOf(false) }


    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )


    val scope = rememberCoroutineScope()
    var sheetContentInitClose by remember { mutableStateOf(false) }

    var sumText = remember {mutableStateOf("0")}
    var lookingInfo =  remember {mutableStateOf(true)}

    if(selectedAccountIndex.value == 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteSurface)

        ) {
            TopBarAccounts(onAddAccountAction = { setSelectedAccount.value = true },
                onNavigationIconClick,
                onFilterClick = { isSelectedFilterAccount.value = true },
                chosenAccountFilter
            )

            BackHandler(enabled = sheetState.isExpanded) {
                scope.launch {
                    sheetState.collapse()
                }
            }



            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    if(sheetContentInitClose){

                        sumText.value = "0"
                        lookingInfo.value = true
                        transactionAccount.value = chosenAccount.value
                        transactionToAccount.value = viewModel.returnAnotherAccount(transactionAccount.value)
                        AccountInfo(transactionAccount = transactionAccount,
                                    transactionToAccount = transactionToAccount,
                                    isForEditing = isForEditing,
                                    selectedAccountIndex = selectedAccountIndex,
                                    collapseBottomSheet = {
                                        scope.launch {
                                            if (sheetState.isExpanded){

                                                sheetState.collapse()
                                            }
                                        }
                                    },
                                    sumText = sumText,
                                    lookingInfo = lookingInfo)
                }
           },
                sheetPeekHeight = 0.dp,
            ){

            if(sheetState.isCollapsed) sheetContentInitClose = true

            val visibleState = remember {
                MutableTransitionState(false).apply {
                    // Start the animation immediately.
                    targetState = true
                }
            }
            AnimatedVisibility(visibleState = visibleState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(whiteSurface)

                ) {
                    SumMoneyInfo(
                        stringResource(R.string.accounts_name_label),
                        viewModel.findSum(
                            viewModel.state.debtAndSimpleList,
                            base = baseCurrency.currencyName
                        ),
                        baseCurrency.currencyName
                    )

                    LazyColumn(
                        modifier = Modifier.weight(2f),
                        contentPadding = PaddingValues()
                    ) {
                        items(
                            items = viewModel.state.debtAndSimpleList,
                            itemContent = {
                                AccountListItem(account = it, navigateToCardSettings = {
                                    scope.launch {
                                        if (sheetState.isExpanded) {
                                            sheetState.collapse()
                                        } else {
                                            chosenAccount.value = it
                                            sheetState.expand()
                                        }
                                    }
                                })
                            }
                        )
                        item {
                            AddBankAccount(
                                AccountsData.accountAdder,
                                navigateToCardAdder = {
                                    isForEditing.value = false;  selectedAccountIndex.value = 1;  chosenAccount.value = AccountsData.normalAccount
                                })
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .padding(0.dp, 16.dp, 0.dp, 0.dp)
                            .background(dividerColor)
                    )

                    SumMoneyInfo(
                        stringResource(R.string.savings_accounts),
                        viewModel.findSum(viewModel.state.goalList, baseCurrency.currencyName),
                        baseCurrency.currencyName
                    )



                    LazyColumn(
                        modifier = Modifier.weight(1.5f),
                        contentPadding = PaddingValues()
                    ) {
                        items(
                            items = viewModel.state.goalList,
                            itemContent = {
                                AccountListItem(
                                    account = it,
                                    navigateToCardSettings = {
                                        scope.launch {
                                            if (sheetState.isExpanded) {
                                                sheetState.collapse()
                                            } else {
                                                chosenAccount.value = it
                                                sheetState.expand()
                                            }
                                        }
                                    })
                            }
                        )
                        item {
                            AddBankAccount(
                                AccountsData.goalAdder,
                                navigateToCardAdder = {
                                    isForEditing.value = false; selectedAccountIndex.value = 1;  chosenAccount.value = AccountsData.goalAccount
                                })
                        }
                    }
                }
            }


        }
    }
    }else if (selectedAccountIndex.value == 1) {
        EditAccount(isForEditing.value, chosenAccount.value ,
            onAddAccountAction = {viewModel.addAccount(it); selectedAccountIndex.value = 0;},
            onDeleteAccount = {viewModel.deleteAccount(it);  selectedAccountIndex.value = 0;},
            onCancelIconClick = {selectedAccountIndex.value = 0;},
            baseCurrency = baseCurrency)

        BackHandler() {
            selectedAccountIndex.value = 0;
        }
    }


    var filterAccounts = (if(viewModel.state.allAccountList.isEmpty()) mutableListOf<Account>() else viewModel.state.allAccountList as MutableList<Account>)
    if(filterAccounts.isNotEmpty() && filterAccounts.get(0).uuid != AccountsData.allAccountFilter.uuid )
        filterAccounts.add(0,AccountsData.allAccountFilter)

    PopUp(openDialog = isSelectedFilterAccount, accountList = filterAccounts, chosenAccountFilter = chosenAccountFilter, onChosenAccountFilterClick = {onChosenAccountFilterClick (it)})

    ChooseAccountCompose (openDialog = setSelectedAccount,
        normalAccount = {setSelectedAccount.value = false ; selectedAccountIndex.value = 1; chosenAccount.value =
            AccountsData.normalAccount;} ,
        debtAccount = {setSelectedAccount.value = false ; selectedAccountIndex.value = 1; chosenAccount.value =
            AccountsData.deptAccount;} ,
        goalAccount = {setSelectedAccount.value = false ; selectedAccountIndex.value = 1; chosenAccount.value =
            AccountsData.goalAccount;})

}


@Composable
fun PopUp(openDialog: MutableState<Boolean>, accountList: MutableList<Account>, chosenAccountFilter: Account , onChosenAccountFilterClick: (Account) -> Unit){
    if(openDialog.value)
        Dialog(

            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            Box( modifier = Modifier.clip(RoundedCornerShape(corner = CornerSize(4.dp)))) {
                ChooseAccount(
                    openDialog = openDialog,
                    accountList = accountList,
                    chosenAccountFilter = chosenAccountFilter,
                    onChosenAccountFilterClick = {onChosenAccountFilterClick(it)}

                )
            }
        }
}

@Composable
private fun ChooseAccount(openDialog: MutableState<Boolean>, accountList: List<Account>, chosenAccountFilter: Account, onChosenAccountFilterClick: (Account) -> Unit,) {
    LazyColumn(
        modifier = Modifier
            .width(210.dp)
            .height(220.dp)
            .background(Color.White)
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
                                openDialog.value = false
                                onChosenAccountFilterClick(it)
                            }
                            .background(if (it == chosenAccountFilter) Color(0xff59aab3) else Color.White)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = it.accountImg.img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(4.dp, 2.dp, 4.dp, 2.dp)
                                .width(60.dp)
                                .height(48.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                        )
                        Column() {
                            Text(it.title, fontSize = 14.sp)
                            val balanceText: String = (if(it.balance>0) "+" else (if(it.balance < 0) "-" else ""))+"${it.currencyType.currency} "+it.balance
                            val balanceColor = if(it.balance>0) currencyColor else (if(it.balance < 0) currencyColorSpent else currencyColorZero)
                            Text(text = balanceText, color = balanceColor, fontSize = 12.sp)
                        }
                    }
                    Divider(modifier = Modifier.background(dividerColor))
                }
            }
        )
    }
}


@Composable
fun ChooseAccountCompose(
    openDialog: MutableState<Boolean>,
    normalAccount: () -> Unit,
    debtAccount: () -> Unit,
    goalAccount: () -> Unit
) {
    if (openDialog.value) {
        Dialog(

            onDismissRequest = {
                openDialog.value = false
            }
        ) {

            Column(modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .fillMaxHeight(0.35f)
                .background(
                    whiteSurface
                ), verticalArrangement = Arrangement.Center) {
                Row(modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Start) {
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = stringResource(R.string.new_account),
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
                Column(modifier = Modifier.padding(18.dp,18.dp,18.dp,0.dp), verticalArrangement = Arrangement.Center) {
                    ChooseAccountElement(
                        stringResource(R.string.common),
                        "${stringResource(R.string.cash)}, ${stringResource(R.string.card)}",
                        painterResource(id = AccountsData.accountImges[0].img),
                        normalAccount
                    )
                    ChooseAccountElement(
                        stringResource(R.string.debt),
                        "${stringResource(R.string.credit)}, ${stringResource(R.string.mortgage)}",
                        painterResource(id = AccountsData.accountImges[6].img),
                        debtAccount
                    )
                    ChooseAccountElement(
                        stringResource(R.string.savings),
                        "${stringResource(R.string.savings)}, ${stringResource(R.string.goal)}, ${stringResource(R.string.purpose)}",
                        painterResource(id = AccountsData.accountImges[7].img),
                        goalAccount
                    )
                }
            }
        }
    }

}




@Composable
fun TopBarAccounts(onAddAccountAction: () -> Unit, onNavigationIconClick: () -> Unit, onFilterClick: () -> Unit,chosenAccountFilter: Account){



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

            Row(modifier = Modifier
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(modifier = Modifier
                    .padding(8.dp, 24.dp, 0.dp, 0.dp)
                    .size(40.dp, 40.dp), onClick = {onNavigationIconClick()}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = whiteSurface,
                        contentDescription = stringResource(R.string.toggle_drawer)
                    )
                }

                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .clickable { onFilterClick() },
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier
                        .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "${stringResource(R.string.filter)} - ${chosenAccountFilter.title} ▾", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                    Text(text = ("${chosenAccountFilter.balance} ${chosenAccountFilter.currencyType.currencyName}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp, 8.dp, 8.dp, 12.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.accounts_name_label), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 15.sp)
                    }
                }

                IconButton(modifier = Modifier
                    .padding(0.dp, 24.dp, 8.dp, 0.dp)
                    .size(40.dp, 40.dp), onClick = { onAddAccountAction ()}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = whiteSurface,
                        contentDescription = stringResource(R.string.toggle_drawer)
                    )
                }

            }
        }
    }
    dividerForTopBar()
}


@Composable
fun SumMoneyInfo(infoText: String , numberOfMoney: Double , currency:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 12.dp, 18.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = infoText, color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 14.sp)

        if (numberOfMoney > 0.0) Text(text = ("$numberOfMoney $currency"), color = currencyColor, fontWeight = FontWeight.W500 , fontSize = 15.sp)
        else if (numberOfMoney < 0.0) Text(text = ("$numberOfMoney $currency"), color = currencyColorSpent, fontWeight = FontWeight.W500 , fontSize = 15.sp)
        else Text(text = ("0.0 $currency"), color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 15.sp)
    }
}

@Composable
fun AddBankAccount(account: Account, navigateToCardAdder: (Account) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 8.dp, 0.dp, 8.dp)
    ) {
        Row(Modifier.clickable {navigateToCardAdder(account) }, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,) {

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                AccountImage(account)
            }

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(0.dp, 8.dp, 0.dp, 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = account.title, fontWeight = FontWeight.W400 ,color = Color.Black, fontSize = 14.sp)
            }
        }
    }

}

@Composable
fun AccountListItem(account: Account, navigateToCardSettings: (Account) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 8.dp, 0.dp, 8.dp)
    ) {
        Row(Modifier.clickable { navigateToCardSettings(account) }, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,) {

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                AccountImage(account)
            }

            Row(
                modifier = Modifier.weight(4f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

            Column(
                modifier = Modifier
                    .padding(0.dp, 8.dp, 0.dp, 8.dp)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                textForAccount(account = account)
            }

                Column(
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(modifier = Modifier.padding(0.dp, 0.dp, 32.dp, 0.dp),text = account.description,maxLines = 2,
                        overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.W400 ,color = currencyColorZero, fontSize = 14.sp)

                    Text(modifier = Modifier.padding(0.dp, 0.dp, 32.dp, 0.dp),
                        text = (if(account.isForDebt) account.debt.toString() else if (account.isForGoal) account.goal.toString() else account.creditLimit.toString() ) + " " + account.currencyType.currency,
                        color =
                        if(account.isForDebt) currencyColorSpent
                        else currencyColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.W400, fontSize = 14.sp)
                }



            }
        }
    }
    CustomDivider()
}
@Composable
private fun CustomDivider(){
    Row(modifier = Modifier.fillMaxWidth()){

        Row(modifier = Modifier.weight(1f)) {}

        Row(modifier = Modifier.weight(4f)) { Divider( modifier = Modifier
            .padding(8.dp, 2.dp, 0.dp, 0.dp)
            .background(dividerColor)) }
    }
}


@Composable
private fun textForAccount(account: Account, modifier: Modifier = Modifier){
    Text(text = account.title, fontWeight = FontWeight.W400 ,color = Color.Black , fontSize = 14.sp)
    Text(modifier = modifier.padding(0.dp, 2.dp, 0.dp, 0.dp) ,
        text =  valueToNormalFormat(account.balance) + " " + account.currencyType.currency,
        color =
        if(account.balance < 0.0) currencyColorSpent
        else if (account.balance > 0.0) currencyColor
        else currencyColorZero,
        fontWeight = FontWeight.W400 , fontSize = 14.sp)
}


@Composable
private fun AccountImage(account: Account) {
    VectorIcon(modifier = Modifier.padding(8.dp),height = 40.dp , width = 50.dp, vectorImg = account.accountImg,onClick = {})
}

@Preview
@Composable
fun PreviewItem() {
   // Accounts(onNavigationIconClick = {})
}
