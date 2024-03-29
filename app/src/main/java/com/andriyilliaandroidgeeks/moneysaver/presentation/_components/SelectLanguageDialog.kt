package com.andriyilliaandroidgeeks.moneysaver.presentation._components

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivity
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.*

val languageList = listOf(
    "Default",
    "Arabic",
    "Chinese",
    "English",
    "French",
    "German",
    "Hindi",
    "Japanese",
    "Spanish",
    "Turkish",
    "Ukrainian"
)

@Composable
fun SelectLanguageDialog(
    openDialog: MutableState<Boolean>,
    appLanguage: MutableState<String?>,
    sharedPref: SharedPreferences,
    restartApp: () -> Unit
    ) {
    if (openDialog.value) {
        val selectedValue = remember { mutableStateOf(languageList[0]) }

        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .height(330.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(backgroundSecondaryColor)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(0.83f).padding(8.dp,12.dp,8.dp,8.dp)
                ) {
                    if(appLanguage.value!=null) selectedValue.value = appLanguage.value!!

                    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
                    val onChangeState: (String) -> Unit = { selectedValue.value = it }

                    items(
                        items = languageList,
                        itemContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .selectable(
                                        selected = isSelectedItem(it),
                                        onClick = { onChangeState(it) },
                                        role = Role.RadioButton
                                    )
                                    .padding(8.dp, 16.dp)
                            ) {
                                RadioButton(

                                    selected = isSelectedItem(it),
                                    onClick = null
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp, 0.dp, 0.dp),
                                    text = it,
                                    fontSize = 20.sp,
                                    color = textPrimaryColor
                                )
                            }
                        }
                    )
                }
                Box(modifier = Modifier.fillMaxSize().background(backgroundSecondaryColor), contentAlignment = Alignment.Center){
                    Button(
                        modifier = Modifier.padding(4.dp,4.dp,4.dp,4.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        onClick = {
                            appLanguage.value = selectedValue.value
                            var editor = sharedPref.edit()
                            editor.putString(MainActivity.APP_LANGUAGE,selectedValue.value)
                            editor.commit()
                            restartApp()
                        }
                    ) {
                        Box(modifier = Modifier.width(70.dp), contentAlignment = Alignment.Center) {
                            Text(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                text = stringResource(R.string.save),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
                }
            }
        }
    }
}