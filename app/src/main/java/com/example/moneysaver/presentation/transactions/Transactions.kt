package com.example.moneysaver.presentation.transactions

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.presentation.accounts.AccountListItem
import com.example.moneysaver.presentation.transactions.additional_composes.BalanceField
import com.example.moneysaver.presentation.transactions.additional_composes.TransactionItem
import com.example.moneysaver.ui.theme.Purple200
import org.intellij.lang.annotations.JdkConstants

@Composable
fun Transactions(
    navigateToTransaction: (Transaction) -> Unit,
    viewModel: TransactionsViewModel = TransactionsViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    viewModel.loadTransactions()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // add transaction
                },
                backgroundColor = Color.Blue
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add transaction")
            }
        }
    ) {


            LazyColumn(
                contentPadding = PaddingValues()
            ) {

                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BalanceField(text = "Starting balance", balance = viewModel.state.startingBalance)
                        Divider(modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp))
                        BalanceField(text = "Ending balance", balance = viewModel.state.endingBalance)
                    }
                    Divider()
                }

                items(
                    items = viewModel.state.transactionList,
                    itemContent = {
                        Column {
                            TransactionItem(transaction = it)
                            Divider()
                        }
                    }
                )

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }

            }

    }
}
