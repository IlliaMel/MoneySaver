package com.example.moneysaver.presentation


import android.annotation.SuppressLint
import android.content.*


import android.os.*
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.Converters
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.data.data_base._test_data.CategoriesData
import com.example.moneysaver.domain.model.Account
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.domain.model.Transaction
import com.example.moneysaver.presentation.MainActivity.Companion.APP_LANGUAGE
import com.example.moneysaver.presentation._components.DrawerBody
import com.example.moneysaver.presentation._components.DrawerHeader
import com.example.moneysaver.presentation._components.SecureCodeEntering
import com.example.moneysaver.presentation._components.SelectLanguageDialog
import com.example.moneysaver.presentation._components.navigation_drawer.MenuBlock
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.presentation._components.notifications.AlarmService
import com.example.moneysaver.presentation._components.time_picker.TimePicker
import com.example.moneysaver.presentation.accounts.Accounts
import com.example.moneysaver.presentation.accounts.AccountsViewModel
import com.example.moneysaver.presentation.accounts.additional_composes.SetAccountCurrencyType
import com.example.moneysaver.presentation.categories.Categories
import com.example.moneysaver.presentation.transactions.Transactions
import com.example.moneysaver.presentation.utils.*
import com.example.moneysaver.ui.theme.MoneySaverTheme
import com.example.moneysaver.ui.theme.lightGrayTransparent
import com.example.moneysaver.ui.theme.whiteSurface
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


data class ImageWithText(
    val image: Painter,
    val text: String
)

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    init {
        instance = this
    }

    companion object {
        var instance: MainActivity? = null
        var isNeedToParseCurrency = false
        var isCategoriesParsed = false
        val APP_LANGUAGE = "app_language"
    }

    private val viewModel: MainActivityViewModel by viewModels()


    val FILE_SELECT_CODE = 0;


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FILE_SELECT_CODE) {
            onChoseImportFileActivityResult(resultCode, data, viewModel)
        }
    }

    private var CURRENCY_PARSED_KEY = "is_currency_parsed"
    private var CURRENCY_PARSING_DATE_KEY = "currency_parsing_date"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SharedPref
        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return

        val lang = sharedPref.getString(APP_LANGUAGE, "Default")
        if(lang!=null) {
            if(lang == "Default") setLanguage(Locale.getDefault().language)
            else setLanguage(Utils.languageToLanguageCode(lang))
        }
        AccountsData.update()
        CategoriesData.update()

        val isParsed = sharedPref.getBoolean(CURRENCY_PARSED_KEY, false)
        val parsingDate = sharedPref.getString(CURRENCY_PARSING_DATE_KEY, "2000-01-01")
        val isConnectionEnabled = Utils.isNetworkAvailable(applicationContext)


        //Formatter of date
        var formattedDate = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            formattedDate = current.format(formatter)
        } else {
            TODO("VERSION.SDK_INT < O")
        }



        //What to do with parsing currencies
        if(isConnectionEnabled){
            isNeedToParseCurrency = !isParsed || parsingDate != formattedDate
        }else{
            if(isParsed){
                isNeedToParseCurrency = false
            }
        }

        //Splash Screen
        installSplashScreen().apply {
            setKeepVisibleCondition {
                viewModel.isLoading.value
            }
        }

        //Delete top bar
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE


        setContent {
            //Set top bar Transparent
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = Color.Transparent
            )


            MoneySaverTheme {
                if ((!isConnectionEnabled && !isParsed) ||  viewModel.isCurrencyDbEmpty()) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(lightGrayTransparent), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_internet_message),
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                } else {
                    val service = AlarmService(context = applicationContext)

                    MainUI(sharedPref,service,formattedDate= formattedDate, context = MainActivity.instance)
                    if (viewModel.isParsingSucceeded.value) {
                        with(sharedPref.edit()) {
                            putBoolean(CURRENCY_PARSED_KEY, true)
                            putString(CURRENCY_PARSING_DATE_KEY, formattedDate)
                            apply()
                        }
                    }
                }
            }
        }
    }

    fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finishAffinity()
    }

}


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainUI(sharedPref: SharedPreferences, alarmService: AlarmService,
           context: Context?,
           formattedDate : String,
           accountsViewModel: AccountsViewModel  = hiltViewModel(),
           viewModel: MainActivityViewModel  = hiltViewModel()){

    // A surface container using the 'background' color from the theme

    var MINUTE_ALARM = "minute_alarm"
    var HOUR_ALARM = "hour_alarm"
    var MAIN_CURRENCY = "main_currency"
    var ALARM_ON = "alarm_on"
    var SECURE_ON = "secure_on"
    var SECURE_CODE = "secure_code"

    /*
        with(sharedPref.edit()) {
            putString(IS_SECURE_ENABLED, formattedDate)
            apply()
        }
        */

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        var selectedTabIndex by remember {
            mutableStateOf(if(sharedPref.getString(SECURE_CODE, "") == "") 0 else 3)
        }

        var baseCurrencyString by remember {
            mutableStateOf(sharedPref.getString(MAIN_CURRENCY, Currency(currency = "₴", currencyName = "UAH", description = "Ukrainian Hryvnia").toString()))
        }

        var baseCurrency by remember {
            mutableStateOf(
                if(baseCurrencyString != null){
                    var arr = baseCurrencyString!!.split(",")
                    Currency(arr[0],arr[1],arr[2],arr[3].toDouble())
                }else{
                    Currency(currency = "₴", currencyName = "UAH",description = "Ukrainian Hryvnia")
                }
            )
        }

        var chosenAccountFilterTMP  = remember { mutableStateOf(AccountsData.allAccountFilter)}

        var chosenAccountFilter =
            if (accountsViewModel.state.allAccountList != null && accountsViewModel.state.allAccountList.isNotEmpty() && chosenAccountFilterTMP.value.uuid != AccountsData.allAccountFilter.uuid)
                accountsViewModel.state.allAccountList.find { it.uuid == chosenAccountFilterTMP.value.uuid }
             else
                chosenAccountFilterTMP.value




        AccountsData.allAccountFilter.balance = accountsViewModel.findSum(accountsViewModel.state.allAccountList,baseCurrency.currencyName)
        AccountsData.allAccountFilter.currencyType = baseCurrency

        var appLanguage = remember {
            mutableStateOf(sharedPref.getString(APP_LANGUAGE, "Default"))
        }

        var hoursNotification = remember {
            mutableStateOf(sharedPref.getInt(HOUR_ALARM, 12))
        }

        var minutesNotification = remember {
            mutableStateOf(sharedPref.getInt(MINUTE_ALARM, 0))
        }

        var timeSwitch by remember {
            mutableStateOf(sharedPref.getBoolean(ALARM_ON, true))
        }

        var secureSwitch by remember {
            mutableStateOf(sharedPref.getBoolean(SECURE_ON, false))
        }

        var notificationClicked by remember {
            mutableStateOf(false)
        }

        var mainCurrencyClicked = remember { mutableStateOf(false) }

        ///*sharedPref.getString(LAST_STARTING, "") != formattedDate && */timeSwitch
        if(timeSwitch)
            alarmService.setAlarm(hoursNotification.value,minutesNotification.value)
        else
            alarmService.cancelAlarm()


        val openSelectLanguageDialog = remember { mutableStateOf(false) }

        var isCodeOpenFromNavBar by remember { mutableStateOf(false) }



        Scaffold(
            scaffoldState = scaffoldState,
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            drawerContent = {
                DrawerHeader()
                if (notificationClicked){
                    TimePicker(TimeParsed = {
                        with(sharedPref.edit()) {
                        putInt(MINUTE_ALARM, minutesNotification.value)
                        putInt(HOUR_ALARM, hoursNotification.value)
                        apply()


                    }
                    }, minutesNotification,hoursNotification)
                    notificationClicked = false
                }

                SetAccountCurrencyType(openDialog = mainCurrencyClicked) { returnType ->
                    mainCurrencyClicked.value = false


                    baseCurrency = viewModel.state.currenciesList.find { currency ->  returnType == currency }!!
                    with(sharedPref.edit()) {
                        putString(MAIN_CURRENCY, baseCurrency.toString())
                        apply()
                    }
                }

                DrawerBody(
                    blocks = listOf(
                        MenuBlock(title = stringResource(R.string.settings), items = listOf(
                            MenuItem(number = 0 , title = stringResource(R.string.language), description = if(appLanguage.value!=null) appLanguage.value!! else stringResource(
                                R.string.defaultStr), icon = Icons.Default.Place),
                            MenuItem(number = 1 , title = stringResource(R.string.theme), description = stringResource(
                                                            R.string.light), icon = Icons.Default.Info),
                            MenuItem(
                                number = 2,
                                title = stringResource(R.string.notifications),
                                description = if (hoursNotification.value <= 9) "0" + hoursNotification.value.toString() else {
                                    hoursNotification.value.toString()
                                } + ":" + if (minutesNotification.value <= 9) "0" + minutesNotification.value.toString() else {
                                    minutesNotification.value.toString()},
                                icon = Icons.Default.Notifications,
                                hasSwitch = true,
                                switchIsActive = timeSwitch
                            ),

                            MenuItem(number = 3 , title = stringResource(R.string.main_currency), description = "${baseCurrency!!.description} ${baseCurrency!!.currencyName} (${baseCurrency!!.currency})", icon = Icons.Default.ShoppingCart),
                            MenuItem(number = 4 , title = stringResource(R.string.secure_code), description = stringResource(R.string.log_in_password), icon = Icons.Default.Lock, hasSwitch = true, switchIsActive = secureSwitch )
                        )

                        ),
                        MenuBlock(title = stringResource(R.string.data_management), items = listOf(
                            MenuItem(number = 5 , title = stringResource(R.string._import), description = stringResource(R.string.read_data_to_file), icon = Icons.Default.Edit),
                            MenuItem(number = 6 , title = stringResource(R.string._export), description = stringResource(
                                                            R.string.write_data_from_file), icon = Icons.Default.Edit)
                        ))
                    ),
                    onItemClick = {
                        when (it.number) {
                            0 -> {
                                openSelectLanguageDialog.value = true
                            }
                            2 -> {
                                notificationClicked = true
                            }
                            3 -> {
                                mainCurrencyClicked.value = true
                            }
                            4 -> {
                                scope.launch {
                                    isCodeOpenFromNavBar = true
                                    selectedTabIndex = 3
                                    scaffoldState.drawerState.close()
                                }

                            }
                            5 -> {
                                scope.launch { scaffoldState.drawerState.close() }
                                importData(viewModel)
                            }
                            6 -> {
                                scope.launch { scaffoldState.drawerState.close() }
                                exportData(viewModel)
                            }
                        }
                    },
                    onSwitchClick = {
                        when (it.number) {
                            2 -> {
                                if(it.switchIsActive)
                                    alarmService.cancelAlarm()
                                else
                                    alarmService.setAlarm(hoursNotification.value,minutesNotification.value)
                                timeSwitch = !timeSwitch
                                with(sharedPref.edit()) {
                                    putBoolean(ALARM_ON, timeSwitch)
                                    apply()}
                            }
                            4 -> {
                                   if(it.switchIsActive){
                                       secureSwitch = false
                                       with(sharedPref.edit()) {
                                           putBoolean(SECURE_ON, false)
                                           putString(SECURE_CODE,"")
                                           apply()}
                                   }else{
                                       scope.launch {
                                           isCodeOpenFromNavBar = true
                                           selectedTabIndex = 3
                                           scaffoldState.drawerState.close()
                                       }
                                   }
                                it.switchIsActive = false
                            }
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
                            },
                            chosenAccountFilter = chosenAccountFilter!!,
                            baseCurrency = baseCurrency,
                            onChosenAccountFilterClick = { account ->
                                chosenAccountFilterTMP.value = account
                            })
                        1 -> Categories(onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }, chosenAccountFilter!!, baseCurrency = baseCurrency)
                        2 -> Transactions(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            },
                            navigateToTransaction = {},
                            chosenAccountFilter!!,
                            viewModel,
                            baseCurrency = baseCurrency
                        )

                        3 -> {

                            SecureCodeEntering(
                                context = context!!,
                                isCorrectFunc = {
                                    selectedTabIndex = 0 },
                                inNewPassword = {
                                    with(sharedPref.edit()) {
                                        putBoolean(SECURE_ON, true)
                                        putString(SECURE_CODE, it)
                                        apply()
                                    }
                                    secureSwitch = true
                                    selectedTabIndex = 0
                                },
                                correctCode = sharedPref.getString(SECURE_CODE, "123456")!!,
                                isCodeOpenFromNavBar,
                            )
                        }

                    }

                }
                if(selectedTabIndex != 3)
                Row(modifier = Modifier.weight(0.8f)) {
                    TabsForScreens(){
                        selectedTabIndex = it
                    }
                }

            }
        }

        SelectLanguageDialog(
            openDialog = openSelectLanguageDialog,
            appLanguage = appLanguage,
            sharedPref = sharedPref,
            restartApp = {MainActivity.instance!!.restartApp()}
        )

        if(viewModel.state.isDataLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }
    }

}

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MoneySaverTheme {
        }
    }


