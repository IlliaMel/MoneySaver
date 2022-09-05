package com.example.moneysaver.presentation.transactions

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.presentation.MainActivityViewModel
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation.transactions.additional_composes.BalanceField
import com.example.moneysaver.presentation.transactions.additional_composes.DateBlock
import com.example.moneysaver.presentation.transactions.additional_composes.TransactionItem
import com.example.moneysaver.ui.theme.dividerColor
import com.example.moneysaver.ui.theme.whiteSurface
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun Transactions(
    onNavigationIconClick: () -> Unit,
    navigateToTransaction: (Transaction) -> Unit,
    chosenAccountFilter: Account,
    viewModel: MainActivityViewModel,
    baseCurrency: Currency,
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

    val bottomSheetNeedOpening = remember{ mutableStateOf(false)}

    val transactionSearchText = remember { mutableStateOf("") }

    viewModel.account = chosenAccountFilter

    if(minDate.value==null || maxDate.value == null)
        viewModel.loadTransactions(minDate, maxDate)
    else
        viewModel.loadTransactions(minDate, maxDate)
    //viewModel.deleteTransactions()
    viewModel.loadCategories()
    viewModel.loadAccounts()

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
            .thenBy { it.categoryUUID })
    }

    var selectedTransaction: MutableState<Transaction?> = remember { mutableStateOf(null) }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteSurface)
    ) {
        var sheetContentInitClose by remember { mutableStateOf(false) }
        TopBarTransactions(onNavigationIconClick = { onNavigationIconClick() }, minDate = minDate, maxDate = maxDate, transactionSearchText=transactionSearchText,chosenAccountFilter, viewModel)
        BottomSheetScaffold(
            scaffoldState = scaffoldState,

            sheetContent = {
                if(sheetContentInitClose)
                if(selectedCategory.value==null && selectedTransaction.value==null) {
                    CategoryChooser(selectedCategory, viewModel.state.categoriesList)
                } else {
                    if(selectedCategory.value==null && selectedTransaction.value!=null)
                        selectedCategory.value = viewModel.state.categoriesList.first{ it.uuid == selectedTransaction.value!!.categoryUUID}
                    val transactionCategory: MutableState<Category> = remember { mutableStateOf(selectedCategory.value!!) }
                    TransactionEditor(
                        currentTransaction = selectedTransaction,
                        category = transactionCategory,
                        addTransaction = viewModel::addTransaction,
                        deleteTransaction = viewModel::deleteTransaction,
                        closeAdder = {
                            scope.launch {
                                sheetState.collapse()
                                selectedCategory.value=null // reset selected category
                                selectedTransaction.value=null
                            }
                        },
                        accountsList = viewModel.state.accountsList,
                        categoriesList = viewModel.state.categoriesList,
                        minDate = minDate,
                        maxDate = maxDate
                    )
                }
            },
            sheetPeekHeight = 0.dp,
            floatingActionButton = {

                if(sheetState.isCollapsed) sheetContentInitClose = true
//??? !sheetState.isAnimationRunning
                FloatingActionButton(
                    onClick = {
                        if(viewModel.addingTransactionIsAllowed()) {
                            scope.launch {
                                selectedCategory.value=null
                                selectedTransaction.value=null
                                if(sheetState.isCollapsed && !sheetState.isAnimationRunning)
                                    scaffoldState.bottomSheetState.expand()
                                else
                                    scaffoldState.bottomSheetState.collapse()
                            }
                        } else showNoAccountOrCategoryMessage()
                    },
                    backgroundColor = if(viewModel.addingTransactionIsAllowed()) Color(0xff5c6bbf) else Color(0xFFADADAB)
                ) {
                    Icon(
                        imageVector = if(sheetState.isCollapsed && !sheetState.isAnimationRunning) Icons.Default.Add else Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(R.string.add_transaction)
                    )
                }
            }
        ) {

            BackHandler(enabled = sheetState.isExpanded) {
                scope.launch {
                    sheetState.collapse()
                    selectedCategory.value=null
                    selectedTransaction.value=null
                }
            }

            if(sheetState.isCollapsed) {
                selectedCategory.value=null
                selectedTransaction.value=null
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
                            baseCurrency,
                            text = stringResource(R.string.starting_balance),
                            balance = viewModel.state.startingBalance
                        )
                        Divider(
                            modifier = Modifier
                                .background(dividerColor)
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                        BalanceField(
                            baseCurrency,
                            text = stringResource(R.string.ending_balance),
                            balance = viewModel.state.endingBalance
                        )
                    }
                    Divider(modifier = Modifier.background(dividerColor))
                }

                for (date: Date in sortedDateToTransactionMap.keys) {
                    var dayBalanceChange = 0.0
                    for (x in sortedDateToTransactionMap[date]!!)
                        dayBalanceChange += x.sum
                    item {
                        DateBlock(baseCurrency = baseCurrency, date = date, balanceChange = dayBalanceChange)
                        Divider(modifier = Modifier.background(dividerColor))
                    }
                    sortedDateToTransactionMap[date]?.let { m ->
                        items(
                            items = m.toList(),
                            key = {it -> it.hashCode()},
                            itemContent = {
                                Column {
                                    val categoryName = viewModel.getCategoryNameByUUIID(it.categoryUUID)

                                    val dismissState = rememberDismissState()
                                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                                        viewModel.deleteTransaction(it)
                                    }

                                    SwipeToDismiss(
                                        state = dismissState,
                                        modifier = Modifier
                                            .padding(0.dp, 1.dp),
                                        directions = setOf(
                                            DismissDirection.EndToStart
                                        ),
                                        dismissThresholds = { direction ->
                                            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                                        },
                                        background = {
                                            val color by animateColorAsState(
                                                when (dismissState.targetValue) {
                                                    DismissValue.Default -> Color.White
                                                    else -> Color.Red
                                                }
                                            )
                                            val alignment = Alignment.CenterEnd
                                            val icon = Icons.Default.Delete

                                            val scale by animateFloatAsState(
                                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                            )

                                            Box(
                                                Modifier
                                                    .fillMaxSize()
                                                    .background(color)
                                                    .padding(20.dp, 0.dp),
                                                contentAlignment = alignment
                                            ) {
                                                Icon(
                                                    icon,
                                                    contentDescription = "Delete Icon",
                                                    modifier = Modifier.scale(scale)
                                                )
                                            }
                                        },
                                        dismissContent = {

                                            TransactionItem(
                                                transaction = it,
                                                categoryName=(categoryName?:""),
                                                accountName = viewModel.state.accountsList.first{ x -> x.uuid == it.accountUUID}.title,
                                                vectorImg =viewModel.state.categoriesList.first{ x -> x.uuid == it.categoryUUID}.categoryImg,
                                                onClick = {
                                                    scope.launch {
                                                        if(sheetState.isExpanded) {
                                                            sheetState.collapse()
                                                        } else {
                                                            selectedCategory.value=null
                                                            selectedTransaction.value = it
                                                            sheetState.expand()
                                                        }
                                                    }
                                                },
                                                viewModel = viewModel
                                            )

                                        }
                                    )
                                    
                                    Divider(modifier = Modifier.background(dividerColor))
                                }
                            }
                        )
                    }
                }


            }

        }
    }
}

fun filterBySearchRequest(transactions: List<Transaction>, searchRequest: String): List<Transaction> {
    return transactions.filter { it.note!=null && it.note.lowercase().contains(searchRequest.lowercase()) }
}

@Composable
fun TopBarTransactions(onNavigationIconClick: () -> Unit, minDate: MutableState<Date?>, maxDate: MutableState<Date?>, transactionSearchText: MutableState<String>,chosenAccountFilter: Account, viewModel: MainActivityViewModel) {
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
                            contentDescription = stringResource(R.string.undo_search)
                        )
                    }
                } else {
                    IconButton(modifier = Modifier
                        .padding(8.dp, 24.dp, 0.dp, 0.dp)
                        .size(40.dp, 40.dp), onClick = { onNavigationIconClick() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            tint = whiteSurface,
                            contentDescription = stringResource(R.string.toggle_drawer)
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
                            placeholder = { Text(text = stringResource(R.string.search), fontSize = 16.sp, color = Color.LightGray) },
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
                            .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "${stringResource(R.string.filter)} - ${chosenAccountFilter.title}", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                        Text(text = ("${chosenAccountFilter.balance} ${chosenAccountFilter.currencyType.currencyName}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
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
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                }

            }

            DateRangePicker(
                minDate = minDate,
                maxDate = maxDate
            )

        }

    }
    dividerForTopBar()

}

fun showNoAccountOrCategoryMessage(){
    Toast.makeText(MoneySaver.applicationContext(), MoneySaver.applicationContext().getString(R.string.no_account_or_category_message), Toast.LENGTH_SHORT).show()
}