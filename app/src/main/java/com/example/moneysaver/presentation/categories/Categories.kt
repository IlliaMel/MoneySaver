package com.example.moneysaver.presentation.categories

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.presentation.categories.additional_composes.PieSampleData
import com.example.moneysaver.ui.theme.currencyColor
import com.example.moneysaver.ui.theme.currencyColorSpent
import com.example.moneysaver.ui.theme.currencyColorZero
import com.example.moneysaver.ui.theme.whiteSurface
import hu.ma.charts.pie.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation.categories.additional_composes.SimpleColors
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Categories(
    onNavigationIconClick: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
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

    val selectedCategory : MutableState<Category> = remember { mutableStateOf(CategoriesData.categoriesList[0]) }

    if(minDate.value==null||maxDate.value==null)
        viewModel.loadCategoriesData()
    else
        viewModel.loadCategoriesDataInDateRange(minDate.value!!, maxDate.value!!)

    TopBarCategories(onNavigationIconClick = { onNavigationIconClick ()}, minDate = minDate, maxDate = maxDate)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            TransactionAdder(
                category = selectedCategory,
                addTransaction = viewModel::addTransaction,
                closeAdder = { scope.launch {sheetState.collapse()} }
            )
        },
        sheetPeekHeight = 0.dp
        ) {

        BackHandler(enabled = sheetState.isExpanded) {
            scope.launch {
                sheetState.collapse()
            }
        }
        
        Column(modifier = Modifier
            .fillMaxSize()
            .background(whiteSurface), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)

            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(12.dp)
                    // .border(BorderStroke(2.dp, Color.Red))
                    ,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategoriesImage(
                        category=CategoriesData.categoriesList[0],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[0]
                            switchBottomSheet(scope, sheetState)
                        }
                    )
                    CategoriesImage(
                        category=CategoriesData.categoriesList[1],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[1]
                            Log.d("TAG", ": "+selectedCategory.value?.title)
                            switchBottomSheet(scope, sheetState)
                        }                    )
                    CategoriesImage(
                        category=CategoriesData.categoriesList[2],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[2]
                            switchBottomSheet(scope, sheetState)
                        }                    )
                    CategoriesImage(
                        category=CategoriesData.categoriesList[3],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[3]
                            switchBottomSheet(scope, sheetState)
                        }
                    )
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    // .border(BorderStroke(2.dp, Color.Red))
                    ,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center) {

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(0.8f), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
                        CategoriesImage(
                            category=CategoriesData.categoriesList[0],
                            viewModel = viewModel,
                            modifier=Modifier.clickable {
                                selectedCategory.value = CategoriesData.categoriesList[0]
                                switchBottomSheet(scope, sheetState)
                            }                        )
                        CategoriesImage(
                            category=CategoriesData.categoriesList[2],
                            viewModel = viewModel,
                            modifier=Modifier.clickable {
                                selectedCategory.value = CategoriesData.categoriesList[2]
                                switchBottomSheet(scope, sheetState)
                            }
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(2f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


                        ChartContainer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .animateContentSize(),
                            spent = "41,1$",
                            bank = "321,84$"
                        ) {
                            PieChart( modifier = Modifier
                                .padding(4.dp)
                                ,sliceWidth = 13.dp
                                ,chartSize = 150.dp
                                ,data = getChartData(viewModel.state.categoriesSums.values.toList().filter { it > 0.0 })[0]){

                            }
                        }

                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxHeight()
                        .weight(0.8f), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
                        CategoriesImage(
                            category=CategoriesData.categoriesList[0],
                            viewModel = viewModel,
                            modifier=Modifier.clickable {
                                selectedCategory.value = CategoriesData.categoriesList[0]
                                switchBottomSheet(scope, sheetState)
                            }                        )
                        CategoriesImage(
                            category=CategoriesData.categoriesList[2],
                            viewModel = viewModel,
                            modifier=Modifier.clickable {
                                selectedCategory.value = CategoriesData.categoriesList[2]
                                switchBottomSheet(scope, sheetState)
                            }                        )
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(12.dp)
                    // .border(BorderStroke(2.dp, Color.Red))
                    ,verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CategoriesImage(
                        category=CategoriesData.categoriesList[0],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[0]
                            switchBottomSheet(scope, sheetState)
                        }
                    )
                    CategoriesImage(
                        category=CategoriesData.categoriesList[1],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[1]
                            switchBottomSheet(scope, sheetState)
                        }
                    )
                    CategoriesImage(
                        category=CategoriesData.categoriesList[2],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[2]
                            switchBottomSheet(scope, sheetState)
                        }
                    )
                    CategoriesImage(
                        category=CategoriesData.categoriesList[3],
                        viewModel = viewModel,
                        modifier=Modifier.clickable {
                            selectedCategory.value = CategoriesData.categoriesList[3]
                            switchBottomSheet(scope, sheetState)
                        }
                    )
                }

            }
        }
    }

}

fun getChartData(categoriesSums: List<Double>): List<PieChartData> {
    return LegendPosition.values().map {
        PieChartData(
            entries = categoriesSums.mapIndexed { idx, value ->
                PieChartEntry(
                    value = value.toFloat(),
                    label = AnnotatedString(com.example.moneysaver.presentation.categories.additional_composes.Categories[idx])
                )
            },
            legendPosition = it,
            colors = SimpleColors,
            legendShape = CircleShape,
        )
    }
}

@Composable
fun ChartContainer(
    modifier: Modifier = Modifier,
    spent: String,
    bank: String,
    chartOffset: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement =  Arrangement.SpaceBetween , horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(0.dp), fontSize = 18.sp, fontWeight = FontWeight.W500, text = "Spending", color = currencyColorZero)
        Spacer(modifier = Modifier.requiredSize(chartOffset))
        content()
        Text(modifier = Modifier.padding(0.dp), fontSize = 15.sp, fontWeight = FontWeight.W400, text = bank, color = currencyColorSpent)
        Text(modifier = Modifier.padding(0.dp), fontSize = 15.sp, fontWeight = FontWeight.W400, text = spent, color = currencyColor)
    }
}

@Composable
fun MyChartParent() {

}

@Composable
fun TopBarCategories(onNavigationIconClick: () -> Unit, minDate: MutableState<Date?>, maxDate: MutableState<Date?>){
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
                        imageVector = Icons.Default.Edit,
                        tint = whiteSurface,
                        contentDescription = "Edit"
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
            painter = painterResource(id = category.categoryImg),
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