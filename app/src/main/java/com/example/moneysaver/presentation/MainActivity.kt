package com.example.moneysaver.presentation

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.presentation.accounts.MainAccountScreen
import com.example.moneysaver.presentation.transactions.Transactions
import com.example.moneysaver.ui.theme.MoneySaverTheme
import com.example.moneysaver.ui.theme.whiteSurface
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        setContent {
            MoneySaverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {



                    val scaffoldState = rememberScaffoldState()
                    val scope = rememberCoroutineScope()



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
                        }
                    ) {
                        MainAccountScreen(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            },
                            navigateToCardSettings = {},navigateToCardAdder = {},navigateToGoalAdder = {})
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