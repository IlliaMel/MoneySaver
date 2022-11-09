package com.andriyilliaandroidgeeks.moneysaver.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.andriyilliaandroidgeeks.moneysaver.MoneySaver
import com.andriyilliaandroidgeeks.moneysaver.data.data_base.Converters
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Account
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Transaction
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivity
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivityViewModel
import java.io.*
import java.util.*

fun importData(viewModel: MainActivityViewModel) {
    MainActivity.instance!!.importActivityData(viewModel)
}

fun exportData(viewModel: MainActivityViewModel) {
    MainActivity.instance!!.exportActivityData(viewModel)
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

fun onChoseImportFileActivityResult(resultCode: Int, data: Intent?, viewModel: MainActivityViewModel) {
    if(resultCode == FragmentActivity.RESULT_OK) {
        if(data==null) {
            return
        }
        val uri = data.data
        if (uri != null) {
            try {
                val input: InputStream? = MoneySaver.applicationContext().contentResolver.openInputStream(uri)
                val r = BufferedReader(InputStreamReader(input))
                var line: String?
                var blockIndex = 0; // 0 - accounts, 1 - categories, 2 - transactions
                var converters = Converters()
                val accounts = mutableListOf<Account>()
                val categories = mutableListOf<Category>()
                val transactions = mutableListOf<Transaction>()
                while (r.readLine().also { line = it } != null) {
                    if(line!!.isEmpty()) {
                        // empty line - block with next data types
                        blockIndex++
                        continue
                    }
                    when(blockIndex) {
                        0 -> {
                            // we need to remove '"' from start of first part and '" ' end of last part
                            val str: List<String> = line!!.split("\", \"")
                            val account = Account(
                                uuid = UUID.fromString(str[0].drop(1)),
                                accountImg = converters.fromStringToVectorImg(str[1])!!,
                                currencyType = converters.fromStringToCurrency(str[2])!!,
                                title = str[3],
                                description = str[4],
                                balance = str[5].toDouble(),
                                creditLimit = str[6].toDouble(),
                                goal = str[7].toDouble(),
                                debt = str[8].toDouble(),
                                isForGoal = str[9].toBoolean(),
                                isForDebt = str[10].toBoolean(),
                                creationDate = converters.fromTimestamp(str[11].dropLast(2).toLong())!!
                            )
                            accounts.add(account)
                        }
                        1 -> {
                            // we need to remove '"' from start of first part and end of last part
                            val str: List<String> = line!!.split("\", \"")
                            val category = Category(
                                uuid = UUID.fromString(str[0].drop(1)),
                                categoryImg = converters.fromStringToVectorImg(str[1])!!,
                                currencyType = converters.fromStringToCurrency(str[2])!!,
                                title = str[3],
                                isForSpendings = str[4].toBoolean(),
                                spent = str[5].toDouble(),
                                creationDate = converters.fromTimestamp(str[6].dropLast(2).toLong())!!,
                            )
                            categories.add(category)
                        }
                        2 -> {
                            val str: List<String> = line!!.split("\", \"")
                            val transaction = Transaction(
                                uuid = UUID.fromString(str[0].drop(1)),
                                sum = str[1].toDouble(),
                                categoryUUID = if(str[2]=="null") null else UUID.fromString(str[2]),
                                accountUUID = UUID.fromString(str[3]),
                                toAccountUUID = if(str[4]=="null") null else UUID.fromString(str[4]),
                                toAccountSum = if(str[5]=="null") null else str[5].toDouble(),
                                date = converters.fromTimestamp(str[6].toLong())!!,
                                note = if(str[7].dropLast(2)=="null") null else str[7].dropLast(2)
                            )
                            transactions.add(transaction)
                        }
                    }
                }

                viewModel.importRepository(accounts, categories, transactions)

                Toast.makeText(
                    MoneySaver.applicationContext(), "\"${uri.path}\" has been read",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    MoneySaver.applicationContext(), "Can't read this file",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

