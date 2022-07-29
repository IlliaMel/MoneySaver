package com.example.moneysaver.presentation

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.presentation.accounts.MainAccountScreen
import com.example.moneysaver.presentation.transactions.Transactions
import com.example.moneysaver.ui.theme.MoneySaverTheme
import com.example.moneysaver.ui.theme.whiteSurface
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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

class MainActivity : ComponentActivity() {
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

                            when(selectedTabIndex) {
                                0 -> MainAccountScreen(onTabSelected = {
                                    selectedTabIndex = it
                                },
                                    onNavigationIconClick = {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    },
                                    navigateToCardSettings = {},navigateToCardAdder = {},navigateToGoalAdder = {})
                                1 -> TabsForScreens(){
                                    selectedTabIndex = it
                                }
                                2 -> Transactions(onNavigationIconClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                },navigateToTransaction = {})
                            }
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
    Box(modifier = modifier
        .fillMaxHeight(1f)
        .fillMaxWidth()){

        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.White,
            contentColor = Color.White,
            modifier = modifier
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
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = item.image,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(43.dp)
                                .height(30.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                        )
                        Text(modifier = Modifier
                            .padding(4.dp), text = item.text , fontSize = 14.sp ,  color = if(selectedTabIndex == index) Color.Black else inactiveColor)
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
        Greeting("Android")
    }
}