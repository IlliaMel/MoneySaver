package com.example.moneysaver.presentation

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
import java.io.File
import java.io.FileOutputStream

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

fun MainActivity.exportActivityData(viewModel: MainActivityViewModel) {
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
            MoneySaver.applicationContext(), "Saved to \"Android/data/com.example.moneysaver/files/moneysaver_data.csv\"",
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        Toast.makeText(
            MoneySaver.applicationContext(), "Can't save current app state",
            Toast.LENGTH_SHORT
        ).show()
    }
}


fun MainActivity.importActivityData(viewModel: MainActivityViewModel) {
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