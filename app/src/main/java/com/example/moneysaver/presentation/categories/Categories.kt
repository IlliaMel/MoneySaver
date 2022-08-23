package com.example.moneysaver.presentation.categories

import android.widget.Toast
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
import com.example.moneysaver.domain.category.Category
import hu.ma.charts.pie.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.example.moneysaver.presentation.categories.additional_composes.EditCategory
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
    viewModel: CategoriesViewModel = hiltViewModel()
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

    //added category adder

    if(!isAddingCategory && !isForEditing) {
        if (viewModel.state.categoriesList.isNotEmpty() && viewModel.state.categoriesList.last().uuid == CategoriesData.addCategory.uuid) {

        } else
            viewModel.state.categoriesList.add(CategoriesData.addCategory)
    }else if(viewModel.state.categoriesList.isNotEmpty() && viewModel.state.categoriesList.last().uuid == CategoriesData.addCategory.uuid)
        viewModel.state.categoriesList.removeLast()


    if(minDate.value==null||maxDate.value==null)
        viewModel.loadCategoriesData()
    else
        viewModel.loadCategoriesDataInDateRange(minDate.value!!, maxDate.value!!)
    viewModel.loadAccounts()

    var sheetContentInitClose by remember { mutableStateOf(false) }

    if(!isAddingCategory){
    TopBarCategories(onNavigationIconClick = { onNavigationIconClick ()}, onEditClick = { if(viewModel.state.categoriesList.size > 1 || isForEditing) isForEditing =
        !isForEditing; }, minDate = minDate, maxDate = maxDate,chosenAccountFilter)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            if(sheetContentInitClose)
            TransactionEditor(
                category = selectedCategory,
                addTransaction = viewModel::addTransaction,
                closeAdder = { scope.launch {sheetState.collapse()} },
                accountsList = viewModel.state.accountsList,
                categoriesList = viewModel.state.categoriesList
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
                                if(viewModel.state.categoriesList.isNotEmpty())
                                    CategoriesVectorImage(viewModel.state.categoriesList[0],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory = {
                                            if(viewModel.state.categoriesList.size == 1 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    viewModel.state.categoriesList[0]
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
                                if(viewModel.state.categoriesList.size > 4)
                                    CategoriesVectorImage(viewModel.state.categoriesList[4],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory = {if(viewModel.state.categoriesList.size == 5 && !isForEditing){
                                            isAddingCategory = true
                                        }else {
                                            selectedCategory.value =
                                                viewModel.state.categoriesList[4]
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
                                if(viewModel.state.categoriesList.size > 6)
                                    CategoriesVectorImage(viewModel.state.categoriesList[6],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(viewModel.state.categoriesList.size == 7 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    viewModel.state.categoriesList[6]
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

                                if(viewModel.state.categoriesList.size > 1)
                                    CategoriesVectorImage(viewModel.state.categoriesList[1],
                                        viewModel,
                                        columnModifier = Modifier.weight(1f),
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory = {if(viewModel.state.categoriesList.size == 2 && !isForEditing){
                                            isAddingCategory = true
                                        }else {
                                            selectedCategory.value =
                                                viewModel.state.categoriesList[1]
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
                                if(viewModel.state.categoriesList.size > 2)
                                    CategoriesVectorImage(viewModel.state.categoriesList[2],
                                        viewModel,
                                        columnModifier = Modifier.weight(1f),
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(viewModel.state.categoriesList.size == 3 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    viewModel.state.categoriesList[2]
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
                                    spent = "0.0$",
                                    bank = "0.0$"
                                ) {
                                    var listOfSums = viewModel.state.categoriesSums.values.toList()
                                    val sum = if(listOfSums.isNotEmpty()) listOfSums.reduce { x, y -> x + y } else 0.0
                                    val list =   getChartData(viewModel.state.categoriesList,if(sum == 0.0) listOf(1.0) else listOfSums.filter { it > 0.0 },sum = sum)[0]
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
                                if(viewModel.state.categoriesList.size > 3)
                                    CategoriesVectorImage(viewModel.state.categoriesList[3],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(viewModel.state.categoriesList.size == 4 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    viewModel.state.categoriesList[3]
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
                                if(viewModel.state.categoriesList.size > 5)
                                    CategoriesVectorImage(viewModel.state.categoriesList[5],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(viewModel.state.categoriesList.size == 6 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    viewModel.state.categoriesList[5]
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
                                if(viewModel.state.categoriesList.size > 7)
                                    CategoriesVectorImage(viewModel.state.categoriesList[7],
                                        viewModel,
                                        modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                        modifierBox = Modifier.padding(4.dp),
                                        onClickCategory =  {
                                            if(viewModel.state.categoriesList.size == 8 && !isForEditing){
                                                isAddingCategory = true
                                            }else {
                                                selectedCategory.value =
                                                    viewModel.state.categoriesList[7]
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
                            userScrollEnabled = viewModel.state.categoriesList.size > 15,
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalArrangement = Arrangement.SpaceAround,
                            // content padding

                            content = {

                                if(viewModel.state.categoriesList.size > 8){
                                    for (i in 8 until viewModel.state.categoriesList.size) {
                                        item{
                                            CategoriesVectorImage(viewModel.state.categoriesList[i],
                                                viewModel,
                                                modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                                modifierBox = Modifier.padding(4.dp),
                                                onClickCategory =  {
                                                    if(viewModel.state.categoriesList.size == i + 1 && !isForEditing){
                                                        isAddingCategory = true
                                                    }else {
                                                        selectedCategory.value =
                                                            viewModel.state.categoriesList[i]
                                                        if(isForEditing)
                                                            isAddingCategory = true
                                                        else {
                                                            if(viewModel.addingTransactionIsAllowed())
                                                                switchBottomSheet(scope, sheetState)
                                                            else
                                                                showNoAccountOrCategoryMessage()                                                        }
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
    }else{

        BackHandler() {
            isForEditing = false
            isAddingCategory = false
        }

        EditCategory(isForEditing,category = if(isForEditing) selectedCategory.value else CategoriesData.defaultCategory ,onAddCategoryAction = {isForEditing = false; viewModel.addCategory(it); isAddingCategory = false},onDeleteCategory = {isForEditing = false;  viewModel.deleteCategory(it); isAddingCategory = false},onCancelIconClick = {isForEditing = false;  isAddingCategory = false})
    }


}

fun getChartData(categoriesList: List<Category>,categoriesSums: List<Double>, sum : Double): List<PieChartData> {
    return LegendPosition.values().map {
        PieChartData(

            entries = categoriesSums.mapIndexed { idx, value ->
                PieChartEntry(
                    value = value.toFloat(),
                    label = AnnotatedString(com.example.moneysaver.presentation.categories.additional_composes.Categories[idx])
                )
            },
            legendPosition = it,
            colors = if(sum == 0.0) listOf(CategoriesData.addCategory.categoryImg.externalColor) else categoriesList.mapIndexed { idx, value -> value.categoryImg.externalColor},
            legendShape = CircleShape,
        )
    }
}

@Composable
private fun CategoriesVectorImage(category: Category, viewModel: CategoriesViewModel, modifierVectorImg:  Modifier = Modifier , modifierBox:  Modifier = Modifier, columnModifier: Modifier = Modifier,onClickCategory : () ->  Unit,  cornerSize : Dp = 60.dp) {
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
        val categorySum = viewModel.state.categoriesSums[category]
        if(categorySum == 0.0)
            color = currencyColorZero

        Text(modifier = Modifier.padding(2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 14.sp, fontWeight = FontWeight.W500, text = (categorySum.toString() + " " + category.currencyType), color = color)
    }

}

@Composable
fun ChartContainer(
    modifier: Modifier = Modifier,
    spent: String,
    bank: String,
    chartOffset: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
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
                    Text(text = ("${chosenAccountFilter.value.balance.toString()} ${chosenAccountFilter.value.currencyType}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
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
private fun CategoriesImage(category: Category, viewModel: CategoriesViewModel,  modifier: Modifier = Modifier) {
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