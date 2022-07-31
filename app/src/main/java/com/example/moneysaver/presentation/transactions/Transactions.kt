package com.example.moneysaver.presentation.transactions

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.presentation.TabsForScreens
import com.example.moneysaver.presentation._components.MonthChooser
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.presentation.transactions.additional_composes.BalanceField
import com.example.moneysaver.presentation.transactions.additional_composes.DateBlock
import com.example.moneysaver.presentation.transactions.additional_composes.TransactionItem
import com.example.moneysaver.presentation.transactions.additional_composes.innerShadow
import com.example.moneysaver.ui.theme.dividerColor
import com.example.moneysaver.ui.theme.whiteSurface
import java.util.*

@Composable
fun Transactions(
    onNavigationIconClick: () -> Unit,
    navigateToTransaction: (Transaction) -> Unit,
    viewModel: TransactionsViewModel = TransactionsViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    viewModel.loadTransactions()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteSurface)
        ) {
            TopBarTransactions(onNavigationIconClick = { onNavigationIconClick ()})
            Scaffold(
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            // add transaction
                        },
                        backgroundColor = Color(0xff5c6bbf)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add transaction"
                        )
                    }
                }
            ) {


                LazyColumn(
                    Modifier.background(whiteSurface),
                    contentPadding = PaddingValues()
                ) {

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .background(whiteSurface),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            BalanceField(
                                text = "Starting balance",
                                balance = viewModel.state.startingBalance
                            )
                            Divider(
                                modifier = Modifier
                                    .background(dividerColor)
                                    .fillMaxHeight()
                                    .width(1.dp)
                            )
                            BalanceField(
                                text = "Ending balance",
                                balance = viewModel.state.endingBalance
                            )
                        }
                        Divider(modifier = Modifier.background(dividerColor))
                    }

                    item{
                        DateBlock(date = Date(2022, 7, 27), balanceChange = 2550.5 )
                        Divider(modifier = Modifier.background(dividerColor))
                    }

                    items(
                        items = viewModel.state.transactionList,
                        itemContent = {
                            Column {
                                TransactionItem(transaction = it)
                                Divider(modifier = Modifier.background(dividerColor))
                            }
                        }
                    )

                    item {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .innerShadow(blur = 4.dp, drawLeft = false, drawRight = false))
                    }

                }

            }
        }
 }

    @Composable
    fun TopBarTransactions(onNavigationIconClick: () -> Unit){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.22f)
        ) {
            Image(
                painter = painterResource(R.drawable.bg5),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){

                Row(modifier = Modifier
                    .padding(0.dp, 30.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
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
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(modifier = Modifier
                            .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "Filter - Cash", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                        Text(text = ("3423 $"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
                    }

                    IconButton(modifier = Modifier
                        .padding(0.dp, 24.dp, 8.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = whiteSurface,
                            contentDescription = "Search"
                        )
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(modifier = Modifier
                        .padding(8.dp, 0.dp, 8.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = whiteSurface,
                            contentDescription = "Prev Month"
                        )
                    }

                    MonthChooser(Date(2022, 7, 31),);

                    IconButton(modifier = Modifier
                        .padding(8.dp, 0.dp, 8.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            tint = whiteSurface,
                            contentDescription = "Next Month"
                        )
                    }
                }


            }
        }
        dividerForTopBar()
    }