package com.example.moneysaver.presentation.accounts

import android.view.WindowManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

import com.example.moneysaver.ui.theme.currencyColor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.moneysaver.R
import com.example.moneysaver.ui.theme.currencyColorZero
import com.example.moneysaver.ui.theme.whiteSurface
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.presentation.TabsForScreens
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.presentation.transactions.TransactionsViewModel
import com.example.moneysaver.ui.theme.dividerColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@Composable
fun Accounts(onAddAccountAction: () -> Unit,
             onNavigationIconClick: () -> Unit,
             navigateToCardSettings: (Account) -> Unit,
             viewModel: AccountsViewModel  = hiltViewModel()
){

    viewModel.loadAccounts()



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteSurface)

    ) {

        TopBarAccounts(onAddAccountAction ,onNavigationIconClick)
        SumMoneyInfo(stringResource(R.string.accounts_name_label),viewModel.loadBankAccountSum(),viewModel.state.currentAccount.currencyType)

        val accounts = remember { viewModel.state.accountList }
        val goals = remember { viewModel.state.goalList }




            LazyColumn(
                modifier = Modifier.weight(2f),
                contentPadding = PaddingValues()
            ) {
                items(
                    items = accounts,
                    itemContent = {
                        AccountListItem(account = it, navigateToCardSettings)
                    }
                )
                item {
                    AddBankAccount(AccountsData.accountAdder, navigateToCardSettings)
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
                    items = goals,
                    itemContent = {
                        AccountListItem(account = it, navigateToCardSettings)
                    }
                )
                item {
                    AddBankAccount(AccountsData.goalAdder, navigateToCardSettings)
                }
            }


    }

}


@Composable
fun TopBarAccounts(onAddAccountAction: () -> Unit, onNavigationIconClick: () -> Unit){
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
                    .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier
                        .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "Filter - Cash", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                    Text(text = ("3423 $"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)

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
        if (numberOfMoney != 0.0) Text(text = ("$numberOfMoney $currency"), color = currencyColor, fontWeight = FontWeight.W500 , fontSize = 15.sp)
        else if (numberOfMoney == 0.0) Text(text = ("0.0 $"), color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 15.sp)
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

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(0.dp, 8.dp, 0.dp, 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                textForAccount(account = account)
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
    Text(modifier = modifier.padding(0.dp, 2.dp, 0.dp, 0.dp) , text = (account.balance.toString() + " " + account.currencyType), color = currencyColor, fontWeight = FontWeight.W400 , fontSize = 14.sp)
}


@Composable
private fun AccountImage(account: Account) {
    Image(
        painter = painterResource(id = account.accountImg),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(2.dp)
            .width(55.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
    )
}

@Preview
@Composable
fun PreviewItem() {
    Accounts(onAddAccountAction = {} , onNavigationIconClick = {},navigateToCardSettings = {})
}
