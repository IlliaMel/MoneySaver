package com.example.moneysaver.presentation


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.presentation._components.*

import com.example.moneysaver.presentation._components.navigation_drawer.MenuBlock
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.presentation._components.notifications.AlarmService
import com.example.moneysaver.presentation._components.time_picker.TimePicker

import com.example.moneysaver.presentation.accounts.Accounts
import com.example.moneysaver.presentation.categories.Categories
import com.example.moneysaver.presentation.transactions.Transactions
import com.example.moneysaver.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


data class ImageWithText(
    val image: Painter,
    val text: String
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepVisibleCondition {
                viewModel.isLoading.value
            }
        }

        //delete top bar
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE


        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Color.Transparent
            )
/*
           */

            MoneySaverTheme {
                val service = AlarmService(context = applicationContext)
                MainUI(service)
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainUI(alarmService: AlarmService){

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        var selectedTabIndex by remember {
            mutableStateOf(0)
        }

        val chosenAccountFilter = remember { mutableStateOf(AccountsData.normalAccount) }


        var hoursNotification = remember {
            mutableStateOf(0)
        }

        var minutesNotification = remember {
            mutableStateOf(0)
        }

        var timeSwitch by remember {
            mutableStateOf(false)
        }

        var notificationClicked by remember {
            mutableStateOf(false)
        }


        //alarmService.setAlarm(hours = hoursNotification.value,minutes = minutesNotification.value)

        Scaffold(
            scaffoldState = scaffoldState,
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            drawerContent = {
                DrawerHeader()
                if (notificationClicked){
                    TimePicker(minutesNotification,hoursNotification)
                    notificationClicked = false
                }

                DrawerBody(
                    blocks = listOf(
                        MenuBlock(title = stringResource(R.string.settings), items = listOf(
                            MenuItem(number = 0 , title = stringResource(R.string.language), description = stringResource(
                                                            R.string.defaultStr), icon = Icons.Default.Place),
                            MenuItem(number = 1 , title = stringResource(R.string.theme), description = stringResource(
                                                            R.string.light), icon = Icons.Default.Info),
                            MenuItem(number = 2 , title = stringResource(R.string.notifications), description = "${hoursNotification.value}:${minutesNotification.value}", icon = Icons.Default.Notifications, hasSwitch = true)
                        )),
                        MenuBlock(title = "Block2", items = listOf(
                            MenuItem(number = 3 , title = "Item21", description = "Desc21", icon = Icons.Default.Edit),
                            MenuItem(number = 4 , title = "Item22", description = "Desc22", icon = Icons.Default.Edit),
                            MenuItem(number = 5 , title = "Item23", description = "Desc23", icon = Icons.Default.Edit)
                        ))
                    ),
                    onItemClick = {
                        if(it.number == 2){
                            if(it.switchIsActive)
                                timeSwitch = true
                            notificationClicked = true
                        }
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
                        0 -> Accounts(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            },chosenAccountFilter)
                        1 -> Categories(onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },chosenAccountFilter)
                        2 -> Transactions(onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }, navigateToTransaction = {},chosenAccountFilter)
                    }
                }
                Row(modifier = Modifier.weight(0.8f)) {
                    TabsForScreens(){
                        selectedTabIndex = it
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
                text = stringResource(R.string.accounts)
            ),
            ImageWithText(
                image = painterResource(id = R.drawable.categories_img),
                text = stringResource(R.string.categories)
            ),
            ImageWithText(
                image = painterResource(id = R.drawable.transactions_img),
                text = stringResource(R.string.transactions)
            )
        )


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
            /*
            ChooseAccountFragmentCompose(
                normalAccount = {},
                debtAccount = {},
                goalAccount = {})
            */

        }
    }
