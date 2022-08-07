package com.example.moneysaver.presentation._components

import android.app.DatePickerDialog
import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.presentation.transactions.additional_composes.getNameOfMonthByNumber
import com.example.moneysaver.presentation.transactions.additional_composes.getYear
import com.example.moneysaver.ui.theme.dividerColor
import com.example.moneysaver.ui.theme.whiteSurface
import java.util.*

@Composable
fun DateRangePicker() {
    val openChoseDateRangeDialog = remember { mutableStateOf(false) }
    val openSelectRangeSubDialog = remember { mutableStateOf(false) }
    val openPickDateSubDialog = remember { mutableStateOf(false) }

    val startDate = remember { mutableStateOf(getCurrentMonthDates().first) }
    val endDate = remember { mutableStateOf(getCurrentMonthDates().second) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .size(40.dp, 40.dp), onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = whiteSurface,
                contentDescription = "Prev"
            )
        }

        DateRangeDisplay(from = startDate.value, to = endDate.value, onClick = {openChoseDateRangeDialog.value=true});

        IconButton(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .size(40.dp, 40.dp), onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                tint = whiteSurface,
                contentDescription = "Next"
            )
        }
    }
    ChoseDateRangeDialog(
        openDialog = openChoseDateRangeDialog,
        openSelectRangeSubDialog = openSelectRangeSubDialog,
        openPickDateSubDialog = openPickDateSubDialog,
        startDate = startDate,
        endDate = endDate
    )
    SelectRangeSubDialog(openDialog = openSelectRangeSubDialog)
    DatePickerDialog(
        openDialog = openPickDateSubDialog,
        startDate = startDate,
        endDate = endDate)
}

@Composable
fun DateRangeDisplay(from: Date, to: Date, onClick: ()->Unit) {
    val startMonthAbr = getNameOfMonthByNumber(from.month).take(3);
    val endMonthAbr = getNameOfMonthByNumber(to.month).take(3);

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(modifier=Modifier.padding(4.dp, 0.dp), imageVector = Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
        var dateText = from.date.toString() +" " + startMonthAbr +" " + getYear(from) +" - "+ to.date.toString() +" " + endMonthAbr +" " + getYear(to)
        if(from.year == to.year &&  from.month == to.month && from.date == to.date)
            dateText = from.date.toString() +" " + startMonthAbr +" " + getYear(from)
        Text(
            text = dateText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.White
        )
        Text(
            text = "▾",
            modifier=Modifier.padding(4.dp, 0.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color= Color.White
        )
    }

}

@Composable
private fun ChoseDateRangeDialog(
    openDialog: MutableState<Boolean>,
    openSelectRangeSubDialog: MutableState<Boolean>,
    openPickDateSubDialog: MutableState<Boolean>,
    startDate: MutableState<Date>,
    endDate: MutableState<Date>
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {

            Column(modifier = Modifier
                .width(300.dp)
                .height(IntrinsicSize.Min)
                .background(Color.White)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable {
                                openSelectRangeSubDialog.value = true
                                openDialog.value = false
                            }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
                        Text(text = "Select range")
                        Text(text = "6 Aug 2021 - 7 Aug 2022", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Divider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "All time")
                    }
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight())
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable {
                                openPickDateSubDialog.value = true
                                openDialog.value = false
                            }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Select day")
                    }
                }

                Divider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min)
                            .clickable {
                                startDate.value = getCurrentWeekDates().first
                                endDate.value = getCurrentWeekDates().second
                                openDialog.value = false
                            }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "7",
                            modifier= Modifier
                                .padding(2.dp)
                                .width(25.dp)
                                .height(25.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = "Week")
                        Text(text = "7 - 13 Aug", fontSize = 14.sp, color = Color.Gray)
                    }
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight())
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min)
                            .clickable {
                                startDate.value = getCurrentDayDates().first
                                endDate.value = getCurrentDayDates().second
                                openDialog.value = false
                            }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "1",
                            modifier= Modifier
                                .padding(2.dp)
                                .width(25.dp)
                                .height(25.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = "Today")
                        Text(text = "August 7", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Divider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Min)
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "365",
                            modifier= Modifier
                                .padding(2.dp)
                                .width(40.dp)
                                .height(25.dp)
                                .clickable {
                                    startDate.value = getCurrentYearDates().first
                                    endDate.value = getCurrentYearDates().second
                                    openDialog.value = false
                                }
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = "Year")
                        Text(text = "Year 2022", fontSize = 14.sp, color = Color.Gray)
                    }
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight())
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "31",
                            modifier= Modifier
                                .padding(2.dp)
                                .width(25.dp)
                                .height(25.dp)
                                .clickable {
                                    startDate.value = getCurrentMonthDates().first
                                    endDate.value = getCurrentMonthDates().second
                                    openDialog.value = false
                                }
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = "Month")
                        Text(text = "August 2022", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectRangeSubDialog(openDialog: MutableState<Boolean>) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(IntrinsicSize.Min)
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Select range", fontWeight = FontWeight.Bold)
                }
                Divider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "From",
                            modifier= Modifier
                                .padding(2.dp)
                                .width(50.dp)
                                .height(25.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = "2 Aug 2022", fontSize = 14.sp, color = Color.Gray)
                    }
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight())
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "To",
                            modifier= Modifier
                                .padding(2.dp)
                                .width(50.dp)
                                .height(25.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = "7 Aug 2022", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                Divider()
                
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { openDialog.value = false }) {
                        Text(text = "OK", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

        }
    }      
}

private fun getCurrentDayDates(): Pair<Date, Date> {
    val start = Date()
    start.hours = 0
    start.minutes = 0
    start.seconds = 0
    val end = Date()
    end.hours = 23
    end.minutes = 59
    end.seconds = 59
    return start to end
}

private fun getCurrentWeekDates(): Pair<Date, Date> {
    val c = Calendar.getInstance()
    // Set the calendar to monday of the current week
    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    val start = c.time
    start.hours = 0
    start.minutes = 0
    start.seconds = 0

    c.add(Calendar.DATE, 6)
    val end = c.time
    end.hours = 23
    end.minutes = 59
    end.seconds = 59
    return start to end
}

private fun getCurrentMonthDates(): Pair<Date, Date> {
    val c = Calendar.getInstance()
    val start = Date()
    start.date = 1
    start.hours = 0
    start.minutes = 0
    start.seconds = 0
    val end = Date()
    end.date = c.getActualMaximum(Calendar.DAY_OF_MONTH)
    end.hours = 23
    end.minutes = 59
    end.seconds = 59
    return start to end
}

private fun getCurrentYearDates(): Pair<Date, Date> {
    val start = Date()
    start.month = 0
    start.date = 1
    start.hours = 0
    start.minutes = 0
    start.seconds = 0
    val end = Date()
    end.month = 11
    end.date = 31
    end.hours = 23
    end.minutes = 59
    end.seconds = 59
    return start to end
}

@Composable
fun DatePickerDialog(openDialog: MutableState<Boolean>, startDate: MutableState<Date>, endDate: MutableState<Date>) {
    if(openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            var year = startDate.value.year
            var month = startDate.value.month
            var date = startDate.value.date
            Column(modifier = Modifier.background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                AndroidView(
                    { CalendarView(it) },
                    modifier = Modifier
                        .wrapContentWidth(),
                    update = { views ->
                        views.setOnDateChangeListener { _, _year, _month, _date ->
                            year = _year
                            month = _month
                            date = _date
                        }
                    }
                )
                Button(onClick = {
                    pickDate(Date(year-1900, month-1, date),startDate, endDate)
                    openDialog.value = false
                },
                    shape = RoundedCornerShape(30.dp)) {
                    Text(text = "OK", color = Color.White)
                }
            }

        }
    }
}

private fun pickDate(setDate: Date, startDate: MutableState<Date>, endDate: MutableState<Date>) {
    val start = setDate
    start.hours = 0
    start.minutes = 0
    start.seconds = 0
    val end = setDate
    end.hours = 23
    end.minutes = 59
    end.seconds = 59
    startDate.value = start
    endDate.value = end
}
