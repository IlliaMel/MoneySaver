package com.andriyilliaandroidgeeks.moneysaver.presentation._components.time_picker

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import java.util.*



@Composable
fun TimePicker(TimeParsed: () -> Unit,minutes: MutableState<Int>, hours: MutableState<Int>){


    val mContext = LocalContext.current

    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            hours.value = mHour
            minutes.value = mMinute
            TimeParsed()
        }, mHour, mMinute, false
    )
    mTimePickerDialog.show()
}

