package com.example.moneysaver.presentation.categories

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.CategoriesData
import com.example.moneysaver.domain.model.Category
import hu.ma.charts.pie.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.presentation.MainActivity
import com.example.moneysaver.presentation.MainActivity.Companion.isCategoriesParsed
import com.example.moneysaver.presentation.MainActivityViewModel
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.example.moneysaver.presentation.categories.additional_composes.EditCategory
import com.example.moneysaver.presentation.categories.additional_composes.SimpleColors
import com.example.moneysaver.presentation.transactions.showNoAccountOrCategoryMessage
import com.example.moneysaver.ui.theme.*
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Categories(
    onNavigationIconClick: () -> Unit,
    chosenAccountFilter: MutableState<Account>,
    viewModel: CategoriesViewModel = hiltViewModel(),
    //viewModel: MainActivityViewModel,
    baseCurrency: Currency,
) {


    val minDate: MutableState<Date?> = remember { mutableStateOf(getCurrentMonthDates().first) }
    val maxDate: MutableState<Date?> = remember { mutableStateOf(getCurrentMonthDates().second) }

    var isAddingCategory by remember { mutableStateOf(false) }
    var isForEditing by remember { mutableStateOf(false) }


    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val selectedCategory : MutableState<Category> = remember { mutableStateOf(CategoriesData.addCategory) }

    viewModel.account = chosenAccountFilter.value
    viewModel.loadAccounts()

    var categories  = viewModel.state.categoriesList
    var categoriesWithAdder  = viewModel.getListWithAdderCategory(isAddingCategory,isForEditing)

    //added category adder

    viewModel.loadCategoriesDataInDateRange(minDate.value!!, maxDate.value!!, base = baseCurrency.currencyName)



    var sheetContentInitClose by remember { mutableStateOf(false) }



 if(!isAddingCategory){
    TopBarCategories(onNavigationIconClick = { onNavigationIconClick ()}, onEditClick = { if(categoriesWithAdder.size > 1 || isForEditing) isForEditing =
        !isForEditing; }, minDate = minDate, maxDate = maxDate,chosenAccountFilter)

   /*  if(!isCategoriesParsed){
         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
             CircularProgressIndicator(modifier = Modifier.size(100.dp))
         }

     } else*/if(true) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            if(sheetContentInitClose && viewModel.state.accountsList.isNotEmpty())
            TransactionEditor(
                category = selectedCategory,
                addTransaction = viewModel::addTransaction,
                closeAdder = { scope.launch {sheetState.collapse()} },
                accountsList = viewModel.state.accountsList,
                categoriesList = viewModel.state.categoriesList,
                minDate = minDate,
                maxDate = maxDate
            )
        },
        sheetPeekHeight = 0.dp
        ) {

        if(sheetState.isCollapsed) sheetContentInitClose = true

        BackHandler(enabled = sheetState.isExpanded) {
            scope.launch {
                sheetState.collapse()
            }
        }

        BoxWithConstraints {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp)
                        .background(whiteSurface),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(lightGrayTransparent),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AnimatedVisibility(visible = isForEditing) {
                                Text(modifier = Modifier.padding(8.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 16.sp, fontWeight = FontWeight.W400, text = stringResource(
                                                                    R.string.chose_category_to_edit), color = Color.Black)
                            }
                        }

                    Row(
                        modifier = Modifier
                            .weight(2.2f)
                            .padding(0.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {

                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.SpaceAround,

                                ) {
                                if(categoriesWithAdder.isNotEmpty())
                                    CategoriesVectorImage(categoriesWithAdder[0],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory = {
                                            if(categoriesWithAdder.size == 1 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value = categoriesWithAdder[0]
                                                if(isForEditing)
                                                    isAddingCategory = true
                                                else {
                                                    if(viewModel.addingTransactionIsAllowed())
                                                        switchBottomSheet(scope, sheetState)
                                                    else
                                                        showNoAccountOrCategoryMessage()                                                }
                                            }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.fillMaxSize()){}
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f),
                                verticalArrangement = Arrangement.SpaceAround

                            ) {
                                if(categoriesWithAdder.size > 4)
                                    CategoriesVectorImage(categoriesWithAdder[4],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory = {if(categoriesWithAdder.size == 5 && !isForEditing){
                                            isAddingCategory = true
                                        }else {
                                            selectedCategory.value =
                                                categoriesWithAdder[4]
                                            if(isForEditing)
                                                isAddingCategory = true
                                            else {
                                                if(viewModel.addingTransactionIsAllowed())
                                                    switchBottomSheet(scope, sheetState)
                                                else
                                                    showNoAccountOrCategoryMessage()                                            }
                                        }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.fillMaxSize()){}
                                }
                                if(categoriesWithAdder.size > 6)
                                    CategoriesVectorImage(categoriesWithAdder[6],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(categoriesWithAdder.size == 7 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    categoriesWithAdder[6]
                                                if(isForEditing)
                                                    isAddingCategory = true
                                                else {
                                                    if(viewModel.addingTransactionIsAllowed())
                                                        switchBottomSheet(scope, sheetState)
                                                    else
                                                        showNoAccountOrCategoryMessage()                                                }
                                            }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.fillMaxSize()){}
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(2f),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(0.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                if(categoriesWithAdder.size > 1)
                                    CategoriesVectorImage(categoriesWithAdder[1],
                                        viewModel,
                                        columnModifier = Modifier.weight(1f),
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory = {if(categoriesWithAdder.size == 2 && !isForEditing){
                                            isAddingCategory = true
                                        }else {
                                            selectedCategory.value =
                                                categoriesWithAdder[1]
                                            if(isForEditing)
                                                isAddingCategory = true
                                            else {
                                                if(viewModel.addingTransactionIsAllowed())
                                                    switchBottomSheet(scope, sheetState)
                                                else
                                                    showNoAccountOrCategoryMessage()                                            }
                                        }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.weight(1f).fillMaxSize()){}
                                }
                                if(categoriesWithAdder.size > 2)
                                    CategoriesVectorImage(categoriesWithAdder[2],
                                        viewModel,
                                        columnModifier = Modifier.weight(1f),
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(categoriesWithAdder.size == 3 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    categoriesWithAdder[2]
                                                if(isForEditing)
                                                    isAddingCategory = true
                                                else {
                                                    if(viewModel.addingTransactionIsAllowed())
                                                        switchBottomSheet(scope, sheetState)
                                                    else
                                                        showNoAccountOrCategoryMessage()                                                }
                                            }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.weight(1f).fillMaxSize()){}
                                }

                            }

                            Row(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxWidth()
                                    .padding(0.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                com.example.moneysaver.presentation.categories.ChartContainer(
                                    modifier = Modifier
                                        .background(whiteSurface)
                                        .animateContentSize(),
                                    spent = viewModel.state.spend.toString() + baseCurrency.currency,
                                    earned = viewModel.state.earned.toString() + baseCurrency.currency
                                ) {

                                    var iSAllZero = viewModel.ifAllCategoriesIsZero()
                                    val list =  getChartData(categories,iSAllZero)[0]
                                    PieChart(
                                        modifier = Modifier,
                                        sliceWidth = 13.dp,
                                        chartSize = 175.dp,
                                        legendOffset = 0.dp,
                                        data = list
                                    ){

                                    }
                                }
                            }

                        }


                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {

                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.SpaceAround

                            ) {
                                if(categoriesWithAdder.size > 3)
                                    CategoriesVectorImage(categoriesWithAdder[3],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(categoriesWithAdder.size == 4 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    categoriesWithAdder[3]
                                                if(isForEditing)
                                                    isAddingCategory = true
                                                else {
                                                    if(viewModel.addingTransactionIsAllowed())
                                                        switchBottomSheet(scope, sheetState)
                                                    else
                                                        showNoAccountOrCategoryMessage()                                                }
                                            }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.fillMaxSize()){}
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f),
                                verticalArrangement = Arrangement.SpaceAround,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                if(categoriesWithAdder.size > 5)
                                    CategoriesVectorImage(categoriesWithAdder[5],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(categoriesWithAdder.size == 6 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    categoriesWithAdder[5]
                                                if(isForEditing)
                                                    isAddingCategory = true
                                                else {
                                                    if(viewModel.addingTransactionIsAllowed())
                                                        switchBottomSheet(scope, sheetState)
                                                    else
                                                        showNoAccountOrCategoryMessage()                                                }
                                            }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.fillMaxSize()){}
                                }
                                if(categoriesWithAdder.size > 7)
                                    CategoriesVectorImage(categoriesWithAdder[7],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(categoriesWithAdder.size == 8 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    categoriesWithAdder[7]
                                                if(isForEditing)
                                                    isAddingCategory = true
                                                else {
                                                    if(viewModel.addingTransactionIsAllowed())
                                                        switchBottomSheet(scope, sheetState)
                                                    else
                                                        showNoAccountOrCategoryMessage()                                                }
                                            }}
                                        ,cornerSize =  60.dp)
                                else{
                                    Box(modifier = Modifier.fillMaxSize()){}
                                }
                            }


                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(2f),
                        verticalArrangement = Arrangement.Top

                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 0.dp),
                            columns = GridCells.Fixed(4),
                            userScrollEnabled = categoriesWithAdder.size > 15,
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalArrangement = Arrangement.SpaceAround,
                            // content padding

                            content = {

                                if(categoriesWithAdder.size > 8){
                                    for (i in 8 until categoriesWithAdder.size) {
                                        item{
                                            CategoriesVectorImage(categoriesWithAdder[i],
                                                viewModel,
                                                modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                                modifierBox = Modifier.padding(4.dp),
                                                onClickCategory =  {
                                                    if(categoriesWithAdder.size == i + 1 && !isForEditing){
                                                        isAddingCategory = true
                                                    }else {
                                                        selectedCategory.value =
                                                            categoriesWithAdder[i]
                                                        if(isForEditing)
                                                            isAddingCategory = true
                                                        else {
                                                            if(viewModel.addingTransactionIsAllowed())
                                                                switchBottomSheet(scope, sheetState)
                                                            else
                                                                showNoAccountOrCategoryMessage()  }
                                                    }},cornerSize =  50.dp)
                                        }
                                    }
                                }

                            }
                        )
                    }
                }
            }
        }

    }
     }
    }else{

        BackHandler() {
            isForEditing = false
            isAddingCategory = false
        }

        EditCategory(isForEditing,category = if(isForEditing) selectedCategory.value else CategoriesData.defaultCategory ,onAddCategoryAction = {isForEditing = false; viewModel.addCategory(it); isAddingCategory = false},onDeleteCategory = {isForEditing = false;  viewModel.deleteCategory(it); isAddingCategory = false},onCancelIconClick = {isForEditing = false;  isAddingCategory = false})
    }

}

fun getChartData(categoriesList: List<Category>, iSAllZero : Boolean): List<PieChartData> {
    return  listOf(
        PieChartData(
            entries = if(!iSAllZero)
                categoriesList.filter { it.spent != 0.0 }.mapIndexed { idx, value ->
                PieChartEntry(
                    value = value.spent.toFloat(),
                    label = AnnotatedString("")
                )
            }
            else listOf(PieChartEntry(
                value = 1f,
                label = AnnotatedString("")
            )),
            legendPosition = LegendPosition.values().last(),
            colors = if(iSAllZero || categoriesList.isEmpty()) listOf(CategoriesData.addCategory.categoryImg.externalColor) else categoriesList.filter { it.spent != 0.0 }.mapIndexed { idx, value -> value.categoryImg.externalColor},
            legendShape = CircleShape,
        ))

}

@Composable
private fun CategoriesVectorImage(category: Category, viewModel: CategoriesViewModel, modifierVectorImg:  Modifier = Modifier, modifierBox:  Modifier = Modifier, columnModifier: Modifier = Modifier, onClickCategory : () ->  Unit, cornerSize : Dp = 60.dp) {
    Column(modifier = columnModifier
        .clickable { onClickCategory() }
        .clip(RoundedCornerShape(CornerSize(4.dp)))
        .padding(4.dp), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 14.sp, fontWeight = FontWeight.W400, text = category.title, color = Color.Black)
        VectorIcon(
            modifierBox,
            modifierVectorImg,
            vectorImg = category.categoryImg,
            onClick = {onClickCategory()},
            width = 56.dp,
            height = 56.dp,
            cornerSize = cornerSize
        )
        var color = currencyColor
        if(category.spent == 0.0)
            color = currencyColorZero
        else if(category.spent < 0.0)
            color = currencyColorSpent

        Text(modifier = Modifier.padding(2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 14.sp, fontWeight = FontWeight.W500, text = (category.spent.toString() + " " + category.currencyType), color = color)
    }

}

@Composable
fun ChartContainer(
    modifier: Modifier = Modifier,
    spent: String,
    earned: String,
    chartOffset: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    var isSpendings = true
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        content()
        Text(modifier = Modifier.padding(0.dp,0.dp,0.dp,32.dp), fontSize = 16.sp, fontWeight = FontWeight.W500,
            text = if (isSpendings) stringResource(R.string.spending) else  stringResource(R.string.spending), color = Color.Black)

        Column(modifier = Modifier.padding(0.dp,32.dp,0.dp,0.dp), verticalArrangement =  Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W400,
                    text = if(isSpendings) spent else earned ,
                    color = currencyColorSpent
                )
                Text(
                    modifier = Modifier.padding(0.dp,2.dp,0.dp,0.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    text = if(isSpendings) earned else spent,
                    color =  if(isSpendings) Color(red = currencyColor.red, blue = currencyColor.blue, green = currencyColor.green, alpha = 0.8f) else Color(red = currencyColorSpent.red, blue = currencyColorSpent.blue, green = currencyColorSpent.green, alpha = 0.8f)
                )
        }
    }
    /*
    Column(modifier = modifier.fillMaxHeight(), verticalArrangement =  Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(8.dp), fontSize = 18.sp, fontWeight = FontWeight.W500, text = stringResource(
                    R.string.spending), color = currencyColorZero)
        Spacer(modifier = Modifier.requiredSize(chartOffset))
        content()
        Row() {
            Text(modifier = Modifier.padding(8.dp), fontSize = 15.sp, fontWeight = FontWeight.W400, text = bank, color = currencyColorSpent)
            Text(modifier = Modifier.padding(8.dp), fontSize = 15.sp, fontWeight = FontWeight.W400, text = spent, color = currencyColor)
        }

    }
    */
}


@Composable
fun TopBarCategories(onNavigationIconClick: () -> Unit, onEditClick: () -> Unit, minDate: MutableState<Date?>, maxDate: MutableState<Date?>,chosenAccountFilter: MutableState<Account>){
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
                        contentDescription = stringResource(R.string.toggle_drawer)
                    )
                }

                Column(modifier = Modifier
                    .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier
                        .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "${stringResource(R.string.filter)} - ${chosenAccountFilter.value.title}", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                    Text(text = ("${chosenAccountFilter.value.balance.toString()} ${chosenAccountFilter.value.currencyType.currencyName}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
                }

                IconButton(modifier = Modifier
                    .padding(0.dp, 24.dp, 8.dp, 0.dp)
                    .size(40.dp, 40.dp), onClick = { onEditClick() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = whiteSurface,
                        contentDescription = stringResource(R.string.edit)
                    )
                }

            }

            DateRangePicker(minDate, maxDate)


        }
    }

    dividerForTopBar()
}

@OptIn(ExperimentalMaterialApi::class)
private fun switchBottomSheet(scope: CoroutineScope, bottomSheetState: BottomSheetState) {
    scope.launch {
        if(bottomSheetState.isCollapsed) {
            bottomSheetState.expand()
        } else {
            bottomSheetState.collapse()
        }
    }
}

@Composable
private fun CategoriesImage(category: Category, viewModel: CategoriesViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(4.dp), fontSize = 13.sp, fontWeight = FontWeight.W400, text = category.title, color = Color.Black)
        Image(
            painter = painterResource(id = category.categoryImg.img),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(4.dp)
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
        )
        var color = currencyColor
        val categorySum = viewModel.state.categoriesSums[category]
        if(categorySum == 0.0)
            color = currencyColorZero

        Text(modifier = Modifier.padding(4.dp), fontSize = 13.sp, fontWeight = FontWeight.W500, text = (categorySum.toString() + " " + category.currencyType), color = color)
    }

}