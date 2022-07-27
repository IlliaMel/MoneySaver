package com.example.moneysaver.presentation.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
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
import com.example.moneysaver.presentation.accounts.additional_composes.baselineHeight
import com.example.moneysaver.ui.theme.currencyColor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.moneysaver.R
import com.example.moneysaver.ui.theme.currencyColorZero
import com.example.moneysaver.ui.theme.whiteSurface


@Composable
fun MainAccountScreen(navigateToCardSettings: (Account) -> Unit, navigateToCardAdder: (Account) -> Unit , navigateToGoalAdder: (Account) -> Unit){

    Column(modifier = Modifier.fillMaxSize().background(whiteSurface)){
            UpperAccountInfo();
            val accounts = remember { AccountsData.accountsList }
            LazyColumn(
                contentPadding = PaddingValues()
            ) {
                items(
                    items = accounts,
                    itemContent = {
                        AccountListItem(account = it, navigateToCardSettings)
                    }
                )
            }
            AddBankAccount(AccountsData.accountAdder,navigateToCardAdder)
            Divider( modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp))
            SavingsInfo()
            AddBankAccount(AccountsData.goalAdder,navigateToGoalAdder)


    }


}

@Composable
fun UpperAccountInfo() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(18.dp,12.dp,18.dp,8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.accounts_name_label), color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 16.sp)
        Text(text = ("3423 $"), color = currencyColor, fontWeight = FontWeight.W500 , fontSize = 16.sp)
    }
}

@Composable
fun SavingsInfo() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(18.dp,12.dp,18.dp,8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.savings_accounts), color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 16.sp)
        Text(text = ("0 $"), color = currencyColorZero, fontWeight = FontWeight.W500 , fontSize = 16.sp)
    }
}

@Composable
fun AddBankAccount(account: Account, navigateToCardAdder: (Account) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 8.dp, 0.dp, 8.dp)
    ) {


        Row(Modifier.clickable { }, verticalAlignment = Alignment.CenterVertically,
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
                    Text(text = account.title, fontWeight = FontWeight.W400 , fontSize = 15.sp)
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
            //.background(whiteSurface)
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

    Row(modifier = Modifier.weight(4f)) { Divider( modifier = Modifier.padding(8.dp, 2.dp, 0.dp, 0.dp)) }
    }
}


@Composable
private fun textForAccount(account: Account, modifier: Modifier = Modifier){
    Text(text = account.title, fontWeight = FontWeight.W400 , fontSize = 15.sp)
    Text(modifier = modifier.padding(0.dp, 2.dp, 0.dp, 0.dp) , text = (account.balance.toString() + " " + account.currencyType), color = currencyColor, fontWeight = FontWeight.W400 , fontSize = 15.sp)
}


@Composable
private fun AccountImage(account: Account) {
        Image(
        painter = painterResource(id = account.accountImg),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(4.dp)
            .width(65.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
    )
}

@Preview
@Composable
fun PreviewPuppyItem() {
    MainAccountScreen(navigateToCardSettings = {},navigateToCardAdder = {},navigateToGoalAdder = {})
}
