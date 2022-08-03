package com.example.moneysaver.presentation

import android.annotation.SuppressLint
import android.icu.text.CaseMap
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentTransaction
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.presentation.accounts.MainAccountScreen
import com.example.moneysaver.presentation.accounts.choose_new_account_fragment.ChooseAccountFragment
import com.example.moneysaver.presentation.categories.Categories
import com.example.moneysaver.presentation.categories.additional_composes.ChartContainer
import com.example.moneysaver.presentation.categories.additional_composes.PieSampleData
import com.example.moneysaver.presentation.categories.additional_composes.PieSimpleScreen
import com.example.moneysaver.presentation.categories.additional_composes.PieStyledScreen
import com.example.moneysaver.presentation.transactions.Transactions
import com.example.moneysaver.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import hu.ma.charts.pie.PieChart
import kotlinx.coroutines.launch
/*
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //show content behind status bar
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //make status bar transparent

        setContent {
            MoneySaverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //Transactions(navigateToTransaction = {})
                    //MainAccountScreen(onNavigationIconClick = {},navigateToCardSettings = {},navigateToCardAdder = {},navigateToGoalAdder = {})
                    AppBar(){

                    }

                }
            }
        }
    }
}
*/

data class ImageWithText(
    val image: Painter,
    val text: String
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //delete top bar
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE


        setContent {
            MoneySaverTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //set top bar transparent
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent
                    )

                    val scaffoldState = rememberScaffoldState()
                    val scope = rememberCoroutineScope()

                    var selectedTabIndex by remember {
                        mutableStateOf(0)
                    }


                    Scaffold(
                        scaffoldState = scaffoldState,
                        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                        drawerContent = {
                            DrawerHeader()
                            DrawerBody(
                                items = listOf(
                                    MenuItem(
                                        id = "accounts",
                                        title = "Accounts",
                                        contentDescription = "Go to accounts menu",
                                        icon = Icons.Default.AccountCircle
                                    ),
                                    MenuItem(
                                        id = "categories",
                                        title = "Categories",
                                        contentDescription = "Go to categories menu",
                                        icon = Icons.Default.Star
                                    ),
                                    MenuItem(
                                        id = "operations",
                                        title = "Operations",
                                        contentDescription = "Go to categories menu",
                                        icon = Icons.Default.ShoppingCart
                                    ),
                                ),
                                onItemClick = {
                                    println("Clicked on ${it.title}")
                                }
                            )
                        },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(whiteSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(10f)
                                    .fillMaxWidth()
                                    .background(whiteSurface)
                            ) {
                                when (selectedTabIndex) {
                                    0 -> MainAccountScreen(
                                        onAddAccountAction = {

                                        },
                                        onNavigationIconClick = {
                                            scope.launch {
                                                scaffoldState.drawerState.open()
                                            }
                                        },
                                        navigateToCardSettings = {},
                                        navigateToCardAdder = {},
                                        navigateToGoalAdder = {})
                                    1 -> Categories(onNavigationIconClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    })
                                    2 -> Transactions(onNavigationIconClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    }, navigateToTransaction = {})
                                }
                            }
                        Row(modifier = Modifier.weight(1f)) {
                            TabsForScreens(){
                                selectedTabIndex = it
                            }
                        }

                    }
                }


                    //PieSimpleScreen()
            }




                //PieStyledScreen()
                //CustomTab()


                    ChooseAccountFragmentCompose(
                        normalAccount = {},
                        debtAccount = {},
                        goalAccount = {})

                }
            }
        }
    }

    @Composable
    fun ChooseAccountFragmentCompose(
        normalAccount: () -> Unit,
        debtAccount: () -> Unit,
        goalAccount: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGrayTransparent),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth(0.75f).clip(RoundedCornerShape(corner = CornerSize(4.dp))).fillMaxHeight(0.35f).background(whiteSurface), verticalArrangement = Arrangement.Center) {
                Row(modifier = Modifier.padding(16.dp,0.dp,0.dp,0.dp), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Start) {
                    Text(
                        modifier = Modifier.padding(0.dp),
                        text = "Новий Рахунок",
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
                Column(modifier = Modifier.padding(18.dp,18.dp,18.dp,0.dp), verticalArrangement = Arrangement.Center) {
                    ChooseAccountElement(
                        "Звичайний",
                        "Готівка, Карта ",
                        painterResource(id = CategoriesData.categoriesList.get(0).categoryImg),
                        normalAccount
                    )
                    ChooseAccountElement(
                        "Звичайний",
                        "Кредит, Іпотека",
                        painterResource(id = CategoriesData.categoriesList.get(0).categoryImg),
                        debtAccount
                    )
                    ChooseAccountElement(
                        "Накопичення",
                        "Заощадження, Мета, Ціль",
                        painterResource(id = CategoriesData.categoriesList.get(0).categoryImg),
                        goalAccount
                    )
                }
            }
        }
    }

    @Composable
    fun ChooseAccountElement(
        title: String,
        subTitle: String,
        img: Painter,
        onClickChooser: () -> Unit
    ) {
        Row(modifier = Modifier.padding(16.dp,4.dp,8.dp,4.dp).fillMaxWidth().clickable(onClick = (onClickChooser)), horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.width(140.dp) , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {

                Text(
                    text = title,
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    text = subTitle,
                    fontWeight = FontWeight.W400,
                    color = currencyColorZero,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                    }

            Image(
                painter = img,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(0.dp,8.dp,8.dp,8.dp)
                    .width(40.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
            )
        }

    }

    @Composable
    fun CustomTab() {

        val inactiveColor = Color(0xFF777777)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            TabRow(
                selectedTabIndex = 0,
                backgroundColor = Color.White,
                contentColor = Color.White,
                modifier = Modifier.shadow(elevation = 5.dp)
            ) {
                Tab(selected = false,
                    selectedContentColor = Color.Black,
                    unselectedContentColor = inactiveColor,
                    onClick = { }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = CategoriesData.categoriesList.get(
                                            0
                                        ).categoryImg
                                    ),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .width(44.dp)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                                )
                            }
                            Column() {
                                Text(
                                    text = "Зарплата",
                                    fontWeight = FontWeight.W500,
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Готівка",
                                    fontWeight = FontWeight.W500,
                                    color = currencyColorZero,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 24.dp),
                                text = "+44.4$",
                                fontWeight = FontWeight.W500,
                                color = currencyColor,
                                fontSize = 16.sp
                            )
                        }


                    }

                }
            }
        }
    }

    @Composable
    fun TabsForScreens(
        modifier: Modifier = Modifier,
        onTabSelected: (selectedIndex: Int) -> Unit
    ) {
        var selectedTabIndex by remember {
            mutableStateOf(0)
        }
        val imageWithTexts = listOf(
            ImageWithText(
                image = painterResource(id = R.drawable.accounts_img),
                text = "Accounts"
            ),
            ImageWithText(
                image = painterResource(id = R.drawable.categories_img),
                text = "Categories"
            ),
            ImageWithText(
                image = painterResource(id = R.drawable.transactions_img),
                text = "Transactions"
            )
        )

        val inactiveColor = Color(0xFF777777)
        Box(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = Color.White,
                contentColor = Color.White,
                modifier = modifier.shadow(elevation = 5.dp)
            ) {
                imageWithTexts.forEachIndexed { index, item ->
                    Tab(selected = selectedTabIndex == index,
                        selectedContentColor = Color.Black,
                        unselectedContentColor = inactiveColor,
                        onClick = {
                            selectedTabIndex = index
                            onTabSelected(index)
                        }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = item.image,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .width(35.dp)
                                    .height(23.dp)
                                    .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                            )
                            Text(
                                modifier = Modifier
                                    .padding(2.dp),
                                text = item.text,
                                fontSize = 12.sp,
                                color = if (selectedTabIndex == index) Color.Black else inactiveColor
                            )
                        }

                    }
                }
            }
        }

    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MoneySaverTheme {
            ChooseAccountFragmentCompose(
                normalAccount = {},
                debtAccount = {},
                goalAccount = {})
        }
    }
