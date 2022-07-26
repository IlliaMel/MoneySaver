package com.example.moneysaver.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moneysaver.presentation._components.*
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.ui.theme.MoneySaverTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        topBar = {
                            AppBar(
                                onNavigationIconClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }
                            )
                        },
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