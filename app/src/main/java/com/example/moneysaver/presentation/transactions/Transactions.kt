package com.example.moneysaver.presentation.transactions

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.transaction.Transaction
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation.transactions.additional_composes.BalanceField
import com.example.moneysaver.presentation.transactions.additional_composes.DateBlock
import com.example.moneysaver.presentation.transactions.additional_composes.TransactionItem
import com.example.moneysaver.presentation.transactions.additional_composes.innerShadow
import com.example.moneysaver.ui.theme.dividerColor
import com.example.moneysaver.ui.theme.whiteSurface
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Transactions(
    onNavigationIconClick: () -> Unit,
    navigateToTransaction: (Transaction) -> Unit,
    chosenAccountFilter: MutableState<Account>,
    viewModel: TransactionsViewModel = hiltViewModel()
) {

    val minDate: MutableState<Date?> = remember { mutableStateOf(getCurrentMonthDates().first) }
    val maxDate: MutableState<Date?> = remember { mutableStateOf(getCurrentMonthDates().second) }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val selectedCategory: MutableState<Category?> = remember { mutableStateOf(null) }

    val transactionSearchText = remember { mutableStateOf("") }

    if(minDate.value==null||maxDate.value==null)
        viewModel.loadTransactions()
    else
        viewModel.loadTransactionsBetweenDates(minDate.value!!, maxDate.value!!)
    //viewModel.deleteTransactions()

    val transactionsDateMap = mutableMapOf<Date, MutableList<Transaction>>()
    for (
        x in
        if(transactionSearchText.value.isNullOrEmpty()) viewModel.state.transactionList
        else filterBySearchRequest(viewModel.state.transactionList, transactionSearchText.value
    )) {
        if (!transactionsDateMap.containsKey(Date(x.date.year, x.date.month, x.date.date)))
            transactionsDateMap[Date(x.date.year, x.date.month, x.date.date)] = mutableListOf()
        transactionsDateMap[Date(x.date.year, x.date.month, x.date.date)]?.add(x)
    }
    val sortedDateToTransactionMap = transactionsDateMap.toSortedMap(reverseOrder())
    for (x in sortedDateToTransactionMap.values) {
        x.sortWith(compareByDescending<Transaction> { it.date }.thenBy { it.sum }.thenBy { it.note }
            .thenBy { it.category.uuid })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteSurface)
    ) {

        TopBarTransactions(onNavigationIconClick = { onNavigationIconClick() }, minDate = minDate, maxDate = maxDate, transactionSearchText=transactionSearchText,chosenAccountFilter)
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                if(selectedCategory.value==null) {
                    CategoryChooser(selectedCategory, CategoriesData.categoriesList)
                } else {
                    val transactionCategory: MutableState<Category> = remember { mutableStateOf(selectedCategory.value!!) }
                    TransactionAdder(
                        category = transactionCategory,
                        addTransaction = viewModel::addTransaction,
                        closeAdder = {
                            scope.launch {
                                sheetState.collapse()
                                selectedCategory.value=null // reset selected category
                            }
                        }
                    )
                }
            },
            sheetPeekHeight = 0.dp,
            floatingActionButton = {
                if(sheetState.isCollapsed && !sheetState.isAnimationRunning) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                selectedCategory.value=null
                                scaffoldState.bottomSheetState.expand()
                            }

                        },
                        backgroundColor = Color(0xff5c6bbf)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add transaction"
                        )
                    }
                } else Box() {} // no fab when bottom sheet isExpanded
            }
        ) {
            BackHandler(enabled = sheetState.isExpanded) {
                scope.launch {
                    sheetState.collapse()
                    selectedCategory.value=null
                }
            }

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

                for (date: Date in sortedDateToTransactionMap.keys) {
                    var dayBalanceChange = 0.0;
                    for (x in sortedDateToTransactionMap[date]!!)
                        dayBalanceChange += x.sum
                    item {
                        DateBlock(date = date, balanceChange = dayBalanceChange)
                        Divider(modifier = Modifier.background(dividerColor))
                    }
                    sortedDateToTransactionMap[date]?.let { m ->
                        items(
                            items = m.toList(),
                            itemContent = {
                                Column {
                                    TransactionItem(transaction = it)
                                    Divider(modifier = Modifier.background(dividerColor))
                                }
                            }
                        )
                    }
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LocalConfiguration.current.screenHeightDp.dp)
                            .background(Color(0xffececec))
                            .innerShadow(blur = 4.dp, drawLeft = false, drawRight = false)
                    )
                }

            }

        }
    }
}

fun filterBySearchRequest(transactions: List<Transaction>, searchRequest: String): List<Transaction> {
    return transactions.filter { it.note!=null && it.note.lowercase().contains(searchRequest.lowercase()) }
}

@Composable
fun TopBarTransactions(onNavigationIconClick: () -> Unit, minDate: MutableState<Date?>, maxDate: MutableState<Date?>, transactionSearchText: MutableState<String>,chosenAccountFilter: MutableState<Account>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        val isSearchOpened = remember { mutableStateOf(false) }
        var searchText by remember { mutableStateOf("") }

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
        ) {

            Row(
                modifier = Modifier
                    .padding(0.dp, 30.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if(isSearchOpened.value) {
                    IconButton(modifier = Modifier
                        .padding(8.dp, 24.dp, 0.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = {
                            searchText=""
                            transactionSearchText.value=""
                            isSearchOpened.value=false
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = whiteSurface,
                            contentDescription = "Undo search"
                        )
                    }
                } else {
                    IconButton(modifier = Modifier
                        .padding(8.dp, 24.dp, 0.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = { onNavigationIconClick() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            tint = whiteSurface,
                            contentDescription = "Toggle drawer"
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(isSearchOpened.value) {
                        val focusManager = LocalFocusManager.current
                        val focusRequester = remember { FocusRequester() }
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {searchText=it},
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                focusManager.clearFocus()
                                transactionSearchText.value = searchText
                            }),
                            placeholder = { Text(text = "Search", fontSize = 16.sp, color = Color.LightGray) },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 16.sp,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent),
                            singleLine = true,
                            maxLines = 1
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Text(modifier = Modifier
                            .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "Filter - ${chosenAccountFilter.value.title}", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                        Text(text = ("${chosenAccountFilter.value.balance.toString()} ${chosenAccountFilter.value.currencyType}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
                    }
                }

                if(!isSearchOpened.value) {
                    IconButton(modifier = Modifier
                        .padding(0.dp, 24.dp, 8.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = {
                        isSearchOpened.value=true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = whiteSurface,
                            contentDescription = "Search"
                        )
                    }
                }

            }

            DateRangePicker(minDate, maxDate)


        }
    }
    dividerForTopBar()
}