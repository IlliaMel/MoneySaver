package com.example.moneysaver.presentation


import android.annotation.SuppressLint
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.presentation.MainActivity.Companion.APP_LANGUAGE
import com.example.moneysaver.presentation._components.DrawerBody
import com.example.moneysaver.presentation._components.DrawerHeader
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
import com.example.moneysaver.ui.theme.MoneySaverTheme
import com.example.moneysaver.ui.theme.inactiveColor
import com.example.moneysaver.ui.theme.lightGrayTransparent
import com.example.moneysaver.ui.theme.whiteSurface
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


data class ImageWithText(
    val image: Painter,
    val text: String
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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


    private val FILE_SELECT_CODE = 0;

    fun importActivityData(viewModel: MainActivityViewModel) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File"),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(
                MoneySaver.applicationContext(), "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun exportActivityData(viewModel: MainActivityViewModel) {
        //Text of the Document
        var textToWrite = ""

        for(account in viewModel.state.accountsList) {
            textToWrite+="$account \n"
        }
        textToWrite+="\n"
        for(category in viewModel.state.categoriesList) {
            textToWrite+="$category \n"
        }
        textToWrite+="\n"
        for(transaction in viewModel.state.transactionList) {
            textToWrite+="$transaction \n"
        }

        //Checking the availability state of the External Storage.
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED != state) {

            //If it isn't mounted - we can't write into it.
            return
        }


        //Create a new file that points to the root directory, with the given name:
        val file = File(getExternalFilesDir(null), "moneysaver_data.csv")


        //This point and below is responsible for the write operation
        var outputStream: FileOutputStream? = null
        try {
            if(file.exists()) file.delete()
            file.createNewFile()
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = FileOutputStream(file, true)
            outputStream.write(textToWrite.toByteArray())
            outputStream.flush()
            outputStream.close()
            Toast.makeText(
                MoneySaver.applicationContext(), "Saved to \"Android/data/com.example.moneysaver/moneysaver_data.csv\"",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FILE_SELECT_CODE) {
            if(resultCode == RESULT_OK) {
                if(data==null) {
                    return
                }
                val uri = data.data
                if (uri != null) {
                    Log.d("TAG", "Selected file:" + uri.path )
                }
            }
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
            else setLanguage(languageToLanguageCode(lang))
        }

        val isParsed = sharedPref.getBoolean(CURRENCY_PARSED_KEY, false)
        val parsingDate = sharedPref.getString(CURRENCY_PARSING_DATE_KEY, "2000-01-01")
        val isConnectionEnabled = isNetworkAvailable(applicationContext)


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

    fun setLanguage(languageCode: String) {
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}

@Composable
fun SecureCodeEntering(context : Context, isCorrectFunc : () -> Unit) {
    // I'm using the same duration for all animations.

    Box(modifier = Modifier.fillMaxSize()){
       var isCorrect = false
        var code = remember{ mutableStateOf("")}
        if(code.value.length == 6){
            isCorrect = code.value == "123456"
        }

        Image(
            painter = painterResource(R.drawable.bg5),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            alignment = Alignment.BottomEnd,
            modifier = Modifier.matchParentSize(),
            colorFilter = ColorFilter.tint(color = AccountsData.normalAccount.accountImg.externalColor, blendMode = BlendMode.Softlight)
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(Color.Transparent)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    painter = painterResource(R.drawable.logo_transparent),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    alignment = Alignment.Center,
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Transparent)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(fontSize = 18.sp, fontWeight = FontWeight.W500, text = "Enter You Password", color = Color.White)
                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
                val animVisibleState = remember { MutableTransitionState(false) }
                    .apply { targetState = true }

                secureCodeFilled(context = context,code.value.length, isCorrect = isCorrect, code,animVisibleState,isCorrectFunc = {isCorrectFunc()})

            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(2.5f)
                .background(Color.Transparent)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {

                var list = listOf("1","2","3","4","5","6","7","8","9","","0")

                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                    columns = GridCells.Fixed(3),
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                    // content padding

                    content = {
                        list.forEach {
                            item {
                                secureCodeButton(symbol = it, onClick = {if (it != "" && code.value.length < 6) {code.value += it ;
                                    vibratePhone(context)} })
                            }
                        }

                        item {
                            secureCodeButton(imageVector = Icons.Filled.KeyboardArrowLeft , onClick = {code.value = code.value.dropLast(1); vibratePhone(context)})
                        }

                    })
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(fontSize = 15.sp, fontWeight = FontWeight.W400, text = "Forget password: ", color = Color.White)
            }



        }

    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun secureCodeFilled(
    context : Context,
    filled: Int = 0,
    isCorrect: Boolean,
    code: MutableState<String>,
    animVisibleState: MutableTransitionState<Boolean>,
    isCorrectFunc: () -> Unit, ){




    if (!animVisibleState.targetState &&
        !animVisibleState.currentState
    ) {
        if(isCorrect)
            isCorrectFunc()
        else{
            vibratePhone(context, 400)
            code.value = ""
        }

    }

    Box(modifier = Modifier
        .fillMaxWidth(), contentAlignment = Alignment.Center){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 0.dp,),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            for (i in 1..6) {
                var color = if((filled!= 6 && i <= filled) || isCorrect) {
                    Color(233, 233, 233, 236)
                }else {
                    Color(233, 233, 233, 53)
                }

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }


        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 0.dp,),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            for (i in 1..6) {
                var color = if(filled == 6 && !isCorrect) {
                    Color(233, 233, 233, 236)
                }else {
                    Color(233, 233, 233, 53)
                }
                AnimatedVisibility(
                    visibleState = animVisibleState,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .animateEnterExit(
                                // Slide in/out the inner box.
                                enter = slideInVertically(animationSpec = tween (500)),
                                exit = slideOutVertically(animationSpec = tween (500))
                            ), contentAlignment =  Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(4.dp)
                                .clip(CircleShape).background(color)){


                            if(filled == 6){
                                animVisibleState.targetState = false
                            }

                        }
                    }
                }
            }
        }
    }

}




@Composable
fun secureCodeButton(symbol : String = "", imageVector: ImageVector?= null , onClick : () -> Unit){

    var modifier = if(symbol != "" || imageVector!= null) Modifier
        .size(75.dp)
        .padding(24.dp, 4.dp, 24.dp, 4.dp)
        .clip(CircleShape)
        .clickable { onClick() }
        .background(Color.Transparent)
    else
        Modifier
            .background(Color.Transparent)
            .size(75.dp)
            .padding(24.dp, 4.dp, 24.dp, 4.dp)
            .clip(CircleShape)

    Box(modifier = modifier, contentAlignment = Alignment.Center)
        {

        if(imageVector == null)
            Text(fontSize = 27.sp, fontWeight = FontWeight.W400, text = symbol, color = Color.White)
        else {
            Icon(
                modifier = Modifier
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .width(40.dp)
                    .height(40.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

}


fun importData(viewModel: MainActivityViewModel) {
    MainActivity.instance!!.importActivityData(viewModel)
}

fun exportData(viewModel: MainActivityViewModel) {
    MainActivity.instance!!.exportActivityData(viewModel)
}


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
            mutableStateOf(/*f(sharedPref.getString(SECURE_CODE, "") == "") 0 else*/ 3)
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

                            MenuItem(number = 3 , title = stringResource(R.string.main_currency), description = "${baseCurrency!!.description} ${baseCurrency!!.currencyName} (${baseCurrency!!.currency})", icon = Icons.Default.ShoppingCart)
                        )),
                        MenuBlock(title = "Data Management", items = listOf(
                            MenuItem(number = 4 , title = "Import", description = "Write data to file", icon = Icons.Default.Edit),
                            MenuItem(number = 5 , title = "Export", description = "Get data from file", icon = Icons.Default.Edit)
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
                                importData(viewModel)
                            }
                            5 -> {
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
                            },chosenAccountFilter = chosenAccountFilter!!,baseCurrency = baseCurrency, onChosenAccountFilterClick = { account ->
                                chosenAccountFilterTMP.value= account
                            })
                        1 -> Categories(onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },chosenAccountFilter!!,baseCurrency = baseCurrency)
                        2 -> Transactions(onNavigationIconClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }, navigateToTransaction = {},chosenAccountFilter!!,viewModel,baseCurrency = baseCurrency)

                        3 -> SecureCodeEntering(context = context!!,isCorrectFunc = {selectedTabIndex = 0} )
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

fun vibratePhone(context: Context?, time : Long  = 80) {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(time)
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

fun languageToLanguageCode(language: String): String {
    return when(language) {
        "Albanian" -> "sq"
        "Arabic" -> "ar"
        "Armenian" -> "hy"
        "Azerbaijani" -> "az"
        "Bosnian" -> "bs"
        "Bulgarian" -> "bg"
        "Chinese" -> "zh"
        "Croatian" -> "hr"
        "Czech" -> "cs"
        "Danish" -> "da"
        "English" -> "en"
        "Esperanto" -> "eo"
        "Estonian" -> "et"
        "Filipino" -> "tl"
        "Finnish" -> "fi"
        "French" -> "fr"
        "Georgian" -> "ka"
        "German" -> "de"
        "Greek" -> "el"
        "Icelandic" -> "is"
        "Indonesian" -> "id"
        "Irish" -> "ga"
        "Italian" -> "it"
        "Japanese" -> "ja"
        "Kazakh" -> "kk"
        "Korean" -> "ko"
        "Latvian" -> "lv"
        "Mongolian" -> "mn"
        "Norwegian" -> "no"
        "Persian" -> "fa"
        "Polish" -> "pl"
        "Portuguese" -> "pt"
        "Serbian" -> "sr"
        "Slovak" -> "sk"
        "Slovenian" -> "sl"
        "Swedish" -> "sv"
        "Turkish" -> "tr"
        "Ukrainian" -> "uk"
        else -> "en"
    }

}
