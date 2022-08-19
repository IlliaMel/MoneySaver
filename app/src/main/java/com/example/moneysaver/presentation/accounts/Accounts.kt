package com.example.moneysaver.presentation.accounts

import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
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
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.domain.account.Account

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.moneysaver.R
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.presentation.TabsForScreens
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.example.moneysaver.presentation.accounts.additional_composes.ChooseAccountElement
import com.example.moneysaver.presentation.accounts.additional_composes.EditAccount
import com.example.moneysaver.presentation.transactions.TransactionsViewModel
import com.example.moneysaver.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@Composable
fun Accounts(onNavigationIconClick: () -> Unit,
             chosenAccountFilter: MutableState<Account>,
             viewModel: AccountsViewModel  = hiltViewModel()
){

    var selectedAccountIndex by remember {
        mutableStateOf(0)
    }

    var chosenAccount by remember {
        mutableStateOf(AccountsData.accountsList.get(0))
    }

    var setSelectedAccount = remember { mutableStateOf(false) }
    var isSelectedFilterAccount = remember { mutableStateOf(false) }

    var isForEditing = remember { mutableStateOf(false) }


    if(selectedAccountIndex == 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteSurface)

        ) {


            TopBarAccounts(onAddAccountAction = { setSelectedAccount.value = true },
                onNavigationIconClick, onFilterClick = {isSelectedFilterAccount.value = true}, chosenAccountFilter)
            SumMoneyInfo(
                stringResource(R.string.accounts_name_label),
                viewModel.loadBankAccountSum(),
                viewModel.state.currentAccount.currencyType
            )

            LazyColumn(
                modifier = Modifier.weight(2f),
                contentPadding = PaddingValues()
            ) {
                items(
                    items = viewModel.state.accountList,
                    itemContent = {
                        AccountListItem(account = it, navigateToCardSettings = {isForEditing.value = true ; selectedAccountIndex = 1; chosenAccount = it})
                    }
                )
                item {
                    AddBankAccount(AccountsData.accountAdder, navigateToCardAdder = {isForEditing.value = false; selectedAccountIndex = 1; chosenAccount = AccountsData.normalAccount})
                }
            }
            Divider(
                modifier = Modifier
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
                    .background(dividerColor)
            )

            SumMoneyInfo(
                stringResource(R.string.savings_accounts),
                viewModel.loadSavingsAccountSum(),
                viewModel.state.currentAccount.currencyType
            )



            LazyColumn(
                modifier = Modifier.weight(1.5f),
                contentPadding = PaddingValues()
            ) {
                items(
                    items = viewModel.state.goalList,
                    itemContent = {
                        AccountListItem(account = it, navigateToCardSettings = {isForEditing.value = true ; selectedAccountIndex = 1; chosenAccount = it} )
                    }
                )
                item {
                    AddBankAccount(AccountsData.goalAdder, navigateToCardAdder = { isForEditing.value = false; selectedAccountIndex = 1; chosenAccount = AccountsData.goalAccount})
                }
            }


        }
    }else if (selectedAccountIndex  == 1) {
        EditAccount(isForEditing.value,chosenAccount ,
            onAddAccountAction = {viewModel.addAccount(it); selectedAccountIndex = 0},
            onDeleteAccount = {viewModel.deleteAccount(it); selectedAccountIndex = 0},
            onCancelIconClick = {selectedAccountIndex = 0} )

        BackHandler() {
            selectedAccountIndex = 0
        }
    }


    PopUp(openDialog = isSelectedFilterAccount,accountList = viewModel.state.simpleList , chosenAccountFilter = chosenAccountFilter)

    ChooseAccountCompose (openDialog = setSelectedAccount,
        normalAccount = {setSelectedAccount.value = false ;selectedAccountIndex = 1;chosenAccount =
            AccountsData.normalAccount;} ,
        debtAccount = {setSelectedAccount.value = false ;selectedAccountIndex = 1;chosenAccount =
            AccountsData.deptAccount;} ,
        goalAccount = {setSelectedAccount.value = false ;selectedAccountIndex = 1;chosenAccount =
            AccountsData.goalAccount;})

}


@Composable
fun PopUp(openDialog: MutableState<Boolean>,accountList: List<Account>,chosenAccountFilter: MutableState<Account>){
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
                    chosenAccountFilter = chosenAccountFilter
                )
            }
        }
}

@Composable
private fun ChooseAccount(openDialog: MutableState<Boolean>, accountList: List<Account>, chosenAccountFilter: MutableState<Account>) {
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
                                chosenAccountFilter.value = it
                            }
                            .background(if (it == chosenAccountFilter.value) Color(0xff59aab3) else Color.White)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = it.accountImg.img),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(4.dp, 2.dp, 12.dp, 2.dp)
                                .width(42.dp)
                                .height(31.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                        )
                        Column() {
                            Text(it.title, fontSize = 14.sp)
                            val balanceText: String = (if(it.balance>0) "+" else (if(it.balance < 0) "-" else ""))+"$ "+it.balance
                            val balanceColor = if(it.balance>0) Color.Green else (if(it.balance < 0) Color.Red else Color.Gray)
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
                        text = "Новий Рахунок",
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
                Column(modifier = Modifier.padding(18.dp,18.dp,18.dp,0.dp), verticalArrangement = Arrangement.Center) {
                    ChooseAccountElement(
                        "Звичайний",
                        "Готівка, Карта ",
                        painterResource(id = AccountsData.accountImges[0].img),
                        normalAccount
                    )
                    ChooseAccountElement(
                        "Звичайний",
                        "Кредит, Іпотека",
                        painterResource(id = AccountsData.accountImges[6].img),
                        debtAccount
                    )
                    ChooseAccountElement(
                        "Накопичення",
                        "Заощадження, Мета, Ціль",
                        painterResource(id = AccountsData.accountImges[7].img),
                        goalAccount
                    )
                }
            }
        }
    }

}




@Composable
fun TopBarAccounts(onAddAccountAction: () -> Unit, onNavigationIconClick: () -> Unit, onFilterClick: () -> Unit,chosenAccountFilter: MutableState<Account>){



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
                        contentDescription = "Toggle drawer"
                    )
                }

                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .clickable { onFilterClick() },
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier
                        .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "Filter - ${chosenAccountFilter.value.title} ▾", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                    Text(text = ("${chosenAccountFilter.value.balance.toString()} ${chosenAccountFilter.value.currencyType}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)

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
                        contentDescription = "Toggle drawer"
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
        else Text(text = ("0.0 $"), color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 15.sp)
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(modifier = Modifier.padding(0.dp, 0.dp, 32.dp, 0.dp),text = account.description,maxLines = 2,
                        overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.W400 ,color = currencyColorZero, fontSize = 14.sp)
                    if(!account.isForGoal && !account.isForDebt)
                    Text(modifier = Modifier.padding(0.dp, 0.dp, 32.dp, 0.dp),text = account.creditLimit.toString() + " " + account.currencyType,maxLines = 2,
                        overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.W400 ,color = currencyColor, fontSize = 14.sp)
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
    Text(modifier = modifier.padding(0.dp, 2.dp, 0.dp, 0.dp) , text = if(account.isForDebt) (account.debt.toString() + " " + account.currencyType) else (account.balance.toString() + " " + account.currencyType), color = if(account.isForDebt) currencyColorSpent else if (account.balance > 0) currencyColor else currencyColorZero, fontWeight = FontWeight.W400 , fontSize = 14.sp)
}


@Composable
private fun AccountImage(account: Account) {
    /*
    Image(
        painter = painterResource(id = account.accountImg.img),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(2.dp)
            .width(55.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
    )
*/
    VectorIcon(modifier = Modifier.padding(8.dp),height = 40.dp , width = 50.dp, vectorImg = account.accountImg,onClick = {})
}

/*

@Composable
fun AccountVectorIcon(modifier: Modifier = Modifier, accountImg : AccountImg, onClick: () -> Unit, width : Dp = 55.dp,  height : Dp = 45.dp,){
    Box(modifier = modifier.padding(0.dp, 0.dp, 0.dp, 0.dp).width(width)
        .height(height).clip(RoundedCornerShape(corner = CornerSize(8.dp))).background(accountImg.externalColor),
        contentAlignment = Alignment.Center){
        Icon(modifier = Modifier
            .clickable{onClick()},
            imageVector = ImageVector.vectorResource(accountImg.img),
            tint = accountImg.innerColor,
            contentDescription = null
        )
    }
}
 */

@Preview
@Composable
fun PreviewItem() {
   // Accounts(onNavigationIconClick = {})
}
