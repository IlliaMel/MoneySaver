package com.example.moneysaver.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.presentation.MainActivity
import com.example.moneysaver.presentation.MainActivityViewModel
import java.io.File
import java.io.FileOutputStream
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