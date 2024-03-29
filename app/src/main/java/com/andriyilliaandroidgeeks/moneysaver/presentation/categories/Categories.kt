package com.andriyilliaandroidgeeks.moneysaver.presentation.categories

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.CategoriesData
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import hu.ma.charts.pie.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData.accountBgImg
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivityViewModel
import com.andriyilliaandroidgeeks.moneysaver.presentation._components.*
import com.andriyilliaandroidgeeks.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.andriyilliaandroidgeeks.moneysaver.presentation.categories.additional_composes.EditCategory
import com.andriyilliaandroidgeeks.moneysaver.presentation.transactions.showNoAccountOrCategoryMessage
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.*
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Categories(
    onNavigationIconClick: () -> Unit,
    chosenAccountFilter: Account,
    viewModel: CategoriesViewModel = hiltViewModel(),
    baseCurrency: Currency,
) {


    val minDate: MutableState<Date?> = remember { mutableStateOf(getCurrentMonthDates().first) }
    val maxDate: MutableState<Date?> = remember { mutableStateOf(getCurrentMonthDates().second) }

    var isAddingCategory = remember { mutableStateOf(false) }
    var isForEditing by remember { mutableStateOf(false) }


    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val scope = rememberCoroutineScope()


    val selectedCategory : MutableState<Category> = remember { mutableStateOf(CategoriesData.addCategory) }

    viewModel.account = chosenAccountFilter
    viewModel.loadAccounts()

    var categories  = viewModel.state.categoriesList.filter { it.isForSpendings == viewModel.state.isForSpendings }
    var categoriesWithAdder  = viewModel.getListWithAdderCategory(isAddingCategory.value,isForEditing)

    //added category adder

    viewModel.loadCategoriesDataInDateRange(minDate.value, maxDate.value, base = baseCurrency.currencyName)

    var sheetContentInitClose by remember { mutableStateOf(false) }

    if(isForEditing){
        BackHandler() {
            isForEditing = false
            isAddingCategory.value = false
        }
    }
if(!isAddingCategory.value){
    Column() {
        TopBarCategories(onNavigationIconClick = { onNavigationIconClick ()}, onEditClick = { if(categoriesWithAdder.size > 1 || isForEditing) isForEditing =
            !isForEditing; }, minDate = minDate, maxDate = maxDate,chosenAccountFilter = chosenAccountFilter)

        val visibleState = remember {
            MutableTransitionState(false).apply {
                // Start the animation immediately.
                targetState = true
            }
        }
        AnimatedVisibility(modifier = Modifier.background(backgroundSecondaryColor), visibleState  = visibleState) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    Divider(modifier = Modifier.background(bordersSecondaryColor))
                    if(sheetContentInitClose && viewModel.state.accountsList.isNotEmpty() && selectedCategory.value != CategoriesData.addCategory)
                        TransactionEditor(
                            category = selectedCategory,
                            addTransaction = viewModel::addTransaction,
                            closeAdder = { scope.launch {sheetState.collapse()} },
                            accountsList = viewModel.state.accountsList,
                            categoriesList = categories,
                            minDate = minDate,
                            maxDate = maxDate,
                            returnCurrencyValue = viewModel::returnCurrencyValue
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
                            .background(backgroundSecondaryColor)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Crossfade(targetState = viewModel.state, animationSpec = tween(1000)) { state ->
                            when(state.isForSpendings){
                                true,false -> CategoryListView(
                                    viewModel = viewModel,
                                    isForEditing = isForEditing,
                                    categoriesWithAdder = categoriesWithAdder,
                                    isAddingCategory = isAddingCategory,
                                    selectedCategory = selectedCategory,
                                    scope = scope,
                                    sheetState = sheetState,
                                    baseCurrency = baseCurrency,
                                    categories = viewModel.state.categoriesList.filter { it.isForSpendings == state.isForSpendings }
                                )
                            }
                        }
                    }
                }
            }
        }
     }
 }
         else{

        BackHandler() {
            isForEditing = false
            isAddingCategory.value = false
        }

        EditCategory(isForSpendings = viewModel.state.isForSpendings,isForEditing,
            category = if(isForEditing) selectedCategory.value else CategoriesData.defaultCategory ,
            onAddCategoryAction = {isForEditing = false; viewModel.addCategory(it); isAddingCategory.value = false},
            onDeleteCategory = {isForEditing = false;  viewModel.deleteCategory(it); isAddingCategory.value = false},
            onCancelIconClick = {isForEditing = false;  isAddingCategory.value = false},
            baseCurrency = baseCurrency)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryListView(
    viewModel: CategoriesViewModel,
    isForEditing: Boolean,
    categoriesWithAdder: List<Category>,
    isAddingCategory: MutableState<Boolean>,
    selectedCategory: MutableState<Category>,
    scope: CoroutineScope,
    sheetState: BottomSheetState,
    baseCurrency: Currency,
    categories: List<Category>
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundPrimaryColor)
            .height(LocalConfiguration.current.screenHeightDp.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundSecondaryColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = isForEditing) {
                Text(modifier = Modifier.padding(4.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 16.sp, fontWeight = FontWeight.W400, text = stringResource(
                    R.string.chose_category_to_edit), color = textPrimaryColor)
            }
        }

        Row(
            modifier = Modifier
                .weight(1.8f)
                .padding(0.dp, 8.dp, 0.dp, 0.dp)
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
                                    isAddingCategory.value = true
                                }else {
                                    selectedCategory.value = categoriesWithAdder[0]
                                    if(isForEditing)
                                        isAddingCategory.value = true
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
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if(categoriesWithAdder.size > 4)
                            CategoriesVectorImage(categoriesWithAdder[4],
                                viewModel,
                                modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                modifierBox = Modifier.padding(4.dp),
                                onClickCategory = {if(categoriesWithAdder.size == 5 && !isForEditing){
                                    isAddingCategory.value = true
                                }else {
                                    selectedCategory.value =
                                        categoriesWithAdder[4]
                                    if(isForEditing)
                                        isAddingCategory.value = true
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
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if(categoriesWithAdder.size > 6)
                            CategoriesVectorImage(categoriesWithAdder[6],
                                viewModel,
                                modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                modifierBox = Modifier.padding(4.dp),
                                onClickCategory =  {
                                    if(categoriesWithAdder.size == 7 && !isForEditing){
                                        isAddingCategory.value = true
                                    }else {
                                        selectedCategory.value =
                                            categoriesWithAdder[6]
                                        if(isForEditing)
                                            isAddingCategory.value = true
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
                                isAddingCategory.value = true
                            }else {
                                selectedCategory.value =
                                    categoriesWithAdder[1]
                                if(isForEditing)
                                    isAddingCategory.value = true
                                else {
                                    if(viewModel.addingTransactionIsAllowed())
                                        switchBottomSheet(scope, sheetState)
                                    else
                                        showNoAccountOrCategoryMessage()                                            }
                            }}
                            ,cornerSize =  60.dp)
                    else{
                        Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()){}
                    }
                    if(categoriesWithAdder.size > 2)
                        CategoriesVectorImage(categoriesWithAdder[2],
                            viewModel,
                            columnModifier = Modifier.weight(1f),
                            modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                            modifierBox = Modifier.padding(4.dp),
                            onClickCategory =  {
                                if(categoriesWithAdder.size == 3 && !isForEditing){
                                    isAddingCategory.value = true
                                }else {
                                    selectedCategory.value =
                                        categoriesWithAdder[2]
                                    if(isForEditing)
                                        isAddingCategory.value = true
                                    else {
                                        if(viewModel.addingTransactionIsAllowed())
                                            switchBottomSheet(scope, sheetState)
                                        else
                                            showNoAccountOrCategoryMessage()                                                }
                                }}
                            ,cornerSize =  60.dp)
                    else{
                        Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()){}
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
                    com.andriyilliaandroidgeeks.moneysaver.presentation.categories.ChartContainer(
                        spent = viewModel.state.spend.toString() + baseCurrency.currency,
                        earned = viewModel.state.earned.toString() + baseCurrency.currency,
                        onClickChart = {
                            viewModel.swapCategoriesType()
                        },
                        isSpendings = viewModel.state.isForSpendings
                    ) {
                        var iSAllZero = viewModel.ifAllCategoriesIsZero()
                        val list =  getChartData(categories,iSAllZero)[0]

                        if(viewModel.state.isForSpendings) {
                            PieChart(
                                modifier = Modifier,
                                sliceWidth = 13.dp,
                                chartSize = 175.dp,
                                legendOffset = 0.dp,
                                data = list
                            ) {

                            }
                        }
                        else{
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
                                    isAddingCategory.value = true
                                }else {
                                    selectedCategory.value =
                                        categoriesWithAdder[3]
                                    if(isForEditing)
                                        isAddingCategory.value = true
                                    else {
                                        if(viewModel.addingTransactionIsAllowed())
                                            switchBottomSheet(scope, sheetState)
                                        else
                                            showNoAccountOrCategoryMessage()                                                }
                                }}
                            ,cornerSize =  60.dp)
                    else{
                        Box(modifier = Modifier.weight(1f)){}
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(2f),
                    verticalArrangement = Arrangement.SpaceAround,

                    ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if(categoriesWithAdder.size > 5)
                            CategoriesVectorImage(categoriesWithAdder[5],
                                viewModel,
                                modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                modifierBox = Modifier.padding(4.dp),
                                onClickCategory =  {
                                    if(categoriesWithAdder.size == 6 && !isForEditing){
                                        isAddingCategory.value = true
                                    }else {
                                        selectedCategory.value =
                                            categoriesWithAdder[5]
                                        if(isForEditing)
                                            isAddingCategory.value = true
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
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if(categoriesWithAdder.size > 7)
                            CategoriesVectorImage(
                                categoriesWithAdder[7],
                                viewModel,
                                modifierVectorImg = Modifier.padding(8.dp, 8.dp, 8.dp, 8.dp),
                                modifierBox = Modifier.padding(4.dp),
                                onClickCategory =  {
                                    if(categoriesWithAdder.size == 8 && !isForEditing){
                                        isAddingCategory.value = true
                                    }else {
                                        selectedCategory.value =
                                            categoriesWithAdder[7]
                                        if(isForEditing)
                                            isAddingCategory.value = true
                                        else {
                                            if(viewModel.addingTransactionIsAllowed())
                                                switchBottomSheet(scope, sheetState)
                                            else
                                                showNoAccountOrCategoryMessage()                                                }
                                    }}
                                ,cornerSize =  60.dp)
                        else{
                            Box(modifier = Modifier.weight(1f)){}
                        }
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
                                            isAddingCategory.value = true
                                        }else {
                                            selectedCategory.value =
                                                categoriesWithAdder[i]
                                            if(isForEditing)
                                                isAddingCategory.value = true
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
        Text(modifier = Modifier.padding(2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 14.sp, fontWeight = FontWeight.W400, text = category.title, color = textPrimaryColor)
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

        Text(modifier = Modifier.padding(2.dp), maxLines = 1, overflow = TextOverflow.Ellipsis ,fontSize = 14.sp, fontWeight = FontWeight.W500, text = (category.spent.toString() + " " + category.currencyType.currency), color = color)
    }

}

@Composable
fun ChartContainer(
    modifier: Modifier = Modifier,
    spent: String,
    earned: String,
    isSpendings : Boolean,
    chartOffset: Dp = 0.dp,
    onClickChart: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column() {
   /* Box(modifier = modifier
        .fillMaxSize()
        .clip(CircleShape), contentAlignment = Alignment.Center) {
        */
        Box( modifier = Modifier
            .size(180.dp, 180.dp)
            .clip(CircleShape)
            .background(backgroundPrimaryColor)
            .clickable { onClickChart() } , contentAlignment = Alignment.Center){
        content()
        Text(modifier = Modifier.padding(0.dp,0.dp,0.dp,32.dp), fontSize = 16.sp, fontWeight = FontWeight.W500,
            text = if (isSpendings) stringResource(R.string.spending) else  stringResource(R.string.earning), color = textPrimaryColor)

        Column(modifier = Modifier.padding(0.dp,32.dp,0.dp,0.dp), verticalArrangement =  Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W400,
                    text = if(isSpendings) spent else earned ,
                    color = if(isSpendings) currencyColorSpent else currencyColor
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
    }
}


@Composable
fun TopBarCategories(mainActivityViewModel: MainActivityViewModel = hiltViewModel(),  onNavigationIconClick: () -> Unit, onEditClick: () -> Unit, minDate: MutableState<Date?>, maxDate: MutableState<Date?>, chosenAccountFilter: Account){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = if (mainActivityViewModel.state.accountBgImgBitmap != null)
                BitmapPainter(mainActivityViewModel.state.accountBgImgBitmap!!.asImageBitmap())
            else painterResource(accountBgImg),
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
                        .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "${stringResource(R.string.filter)} - ${chosenAccountFilter.title}", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                    Text(text = ("${chosenAccountFilter.balance.toString()} ${chosenAccountFilter.currencyType.currencyName}"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
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
