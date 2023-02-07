package com.andriyilliaandroidgeeks.moneysaver.presentation.transactions.additional_composes

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.VectorImg
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivityViewModel
import com.andriyilliaandroidgeeks.moneysaver.presentation.accounts.AccountsViewModel
import com.andriyilliaandroidgeeks.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.*
import kotlin.math.abs

@Composable
fun TransactionItem(transaction: Transaction,
                    categoryName: String,
                    accountName: String,
                    vectorImg: VectorImg,
                    onClick: ()->Unit,
                    viewModel: MainActivityViewModel,
                    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundPrimaryColor)
            .clickable { onClick() }
            .padding(6.dp,0.dp,4.dp,0.dp),
        //.background(whiteSurface)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            VectorIcon(modifier = Modifier.padding(4.dp), modifierIcn = Modifier.padding(8.dp), height = 45.dp , width = 45.dp, vectorImg = vectorImg, onClick = {}, cornerSize = 50.dp)


            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(8.dp, 8.dp, 12.dp, 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(categoryName, color = textPrimaryColor, fontSize = 16.sp , fontWeight = FontWeight.W400)
                    var sumText= (if(transaction.sum>0) "+" else "-") + (viewModel.state.accountsList.find { it.uuid == transaction.accountUUID }?.currencyType?.currency
                        ?: "$") + " " + abs(transaction.sum)
                    if(transaction.toAccountUUID!=null)
                        sumText+=" -> "+(if(transaction.toAccountSum!!>0) "+" else "-") + (viewModel.state.accountsList.find { it.uuid == transaction.toAccountUUID }?.currencyType?.currency
                            ?: "$") + " " + abs(transaction.toAccountSum!!)

                    val color =  if(transaction.categoryUUID != null) {if(transaction.sum>0) currencyColor else currencyColorSpent} else currencyColorZero
                    Text(sumText, color = color, fontSize = 15.sp , fontWeight = FontWeight.W400)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    var img = (accountsViewModel.state.allAccountList.find { it.uuid == transaction.accountUUID }?.accountImg ?: AccountsData.normalAccount.accountImg)
                    Icon(modifier = Modifier.size(35.dp,28.dp).padding(4.dp,0.dp,8.dp,0.dp),
                        imageVector = ImageVector.vectorResource(img.img),
                        tint = img.externalColor,
                        contentDescription = null
                    )
                    Text(accountName, color = currencyColorZero, fontSize = 16.sp , fontWeight = FontWeight.W400)
                }
                (transaction.note)?.let{
                    Text(transaction.note, color = currencyColorZero, fontSize = 14.sp , fontWeight = FontWeight.W400)
                }
            }
        }

    }
}