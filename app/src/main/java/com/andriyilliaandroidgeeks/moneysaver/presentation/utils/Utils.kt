package com.andriyilliaandroidgeeks.moneysaver.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.ui.graphics.Color
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivity
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.*
import java.util.*

class Utils {
    companion object Methods {
         fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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


        fun languageToLanguageCode(language: String): String {
            return when (language) {
                "English" -> "en"
                "Ukrainian" -> "uk"
                "German" -> "de"
                "French" -> "fr"
                "Spanish" -> "es"
                "Chinese" -> "zh"
                "Hindi" -> "hi"
                "Japanese" -> "ja"
                "Arabic" -> "ar"
                "Turkish" -> "tr"
                else -> "en"
            }
        }
    }
}

fun MainActivity.changeTheme(theme : String){
    if(theme == "dark"){

        textPrimaryColor = Color(0xFFFFFFFF)
        textSecondaryColor = Color(0xFFDDDDDD)

        backgroundPrimaryColor = Color(0xFF141414)
        backgroundSecondaryColor = Color(0xFF1F1F1F)

        bordersPrimaryColor = Color(0xFF292929)
        bordersSecondaryColor = Color(0xFF3A3A3A)

        calculatorButton = Color(23, 23, 23, 225)
        calculatorButtonNumbers = Color(49, 49, 49, 221)
        calculatorBorderColor = Color(0xFF3A3A3A)

    }else if (theme == "light"){

        textPrimaryColor = Color(0xFF141414)
        textSecondaryColor = Color(0xFF1F1F1F)

        backgroundPrimaryColor = Color(0xFFF3F3F3)
        backgroundSecondaryColor = Color(0xFFE7E7E7)

        bordersPrimaryColor = Color(0xFFD2D2D2)
        bordersSecondaryColor = Color(0xFFCACACA)

        calculatorButton = Color(236, 236, 236, 225)
        calculatorButtonNumbers = Color(255, 255, 255, 221)
        calculatorBorderColor = Color(209, 209, 209, 212)
    }

}

fun MainActivity.setLanguage(languageCode: String) {
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