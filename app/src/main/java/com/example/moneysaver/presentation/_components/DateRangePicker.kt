package com.example.moneysaver.presentation._components

import android.content.Context
import android.widget.CalendarView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.R
import com.example.moneysaver.presentation.transactions.additional_composes.getNameOfMonthByNumber
import com.example.moneysaver.presentation.transactions.additional_composes.getYear
import com.example.moneysaver.ui.theme.whiteSurface
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun DateRangePicker(minDate: MutableState<Date?>, maxDate: MutableState<Date?>) {
    val openChoseDateRangeDialog = remember { mutableStateOf(false) }
    val openSelectRangeSubDialog = remember { mutableStateOf(false) }
    val openPickDateSubDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .size(40.dp, 40.dp), onClick = { swapDates(startDate = minDate, endDate = maxDate, swapRight = false)}) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = whiteSurface,
                contentDescription = stringResource(R.string.previous)
            )
        }

        DateRangeDisplay(fromDateState = minDate, toDateState = maxDate, onClick = {openChoseDateRangeDialog.value=true});

        IconButton(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .size(40.dp, 40.dp), onClick = { swapDates(startDate = minDate, endDate = maxDate, swapRight = true)}) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                tint = whiteSurface,
                contentDescription = stringResource(R.string.next)
            )
        }
    }
    ChoseDateRangeDialog(
        openDialog = openChoseDateRangeDialog,
        openSelectRangeSubDialog = openSelectRangeSubDialog,
        openPickDateSubDialog = openPickDateSubDialog,
        startDate = minDate,
        endDate = maxDate
    )
    SelectRangeSubDialog(
        openDialog = openSelectRangeSubDialog,
        startDate = minDate,
        endDate = maxDate
    )
    DatePickerDialog(
        openDialog = openPickDateSubDialog,
        startDate = minDate,
        endDate = maxDate)
}

@Composable
fun DateRangeDisplay(fromDateState: MutableState<Date?>, toDateState: MutableState<Date?>, onClick: ()->Unit) {
    if(fromDateState.value!=null && toDateState.value!=null) {
        val from = fromDateState.value!!
        val to = toDateState.value!!

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(modifier=Modifier.padding(4.dp, 0.dp), imageVector = Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
            Text(
                text = getShortDateRangeString(from, to, LocalContext.current),
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
    } else {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(modifier=Modifier.padding(4.dp, 0.dp), imageVector = Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
            Text(
                text = stringResource(R.string.all_time),
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

}

@Composable
private fun ChoseDateRangeDialog(
    openDialog: MutableState<Boolean>,
    openSelectRangeSubDialog: MutableState<Boolean>,
    openPickDateSubDialog: MutableState<Boolean>,
    startDate: MutableState<Date?>,
    endDate: MutableState<Date?>
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
                        Text(text = stringResource(R.string.select_range))
                        val (d1, d2) = getCurrentMonthDates()
                        var shortRangeText = getShortDateRangeString(d1, d2, LocalContext.current)
                        if(startDate.value!=null && endDate!=null)
                            shortRangeText = getShortDateRangeString(startDate.value!!, endDate.value!!, LocalContext.current)
                        Text(text = shortRangeText, fontSize = 14.sp, color = Color.Gray)
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
                            .clickable {
                                startDate.value = null
                                endDate.value = null
                                openDialog.value = false
                            }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.all_time))
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
                        Text(text = stringResource(R.string.select_day))
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
                        Text(text = stringResource(R.string.Week))
                        val (d1, d2) = getCurrentWeekDates()
                        val weekShortText = d1.date.toString()+" "+ getMonthAbr(d1, LocalContext.current)+" - "+d2.date.toString()+" "+ getMonthAbr(d2, LocalContext.current)
                        Text(text = weekShortText, fontSize = 14.sp, color = Color.Gray)
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
                        Text(text = stringResource(R.string.today))
                        val d1 = getCurrentDayDates().first
                        val todayShortText = getNameOfMonthByNumber(d1.month, LocalContext.current)+" "+d1.date.toString()
                        Text(text = todayShortText, fontSize = 14.sp, color = Color.Gray)
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
                                startDate.value = getCurrentYearDates().first
                                endDate.value = getCurrentYearDates().second
                                openDialog.value = false
                            }
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
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color.White
                        )
                        Text(text = stringResource(R.string.year))
                        val d1 = Date()
                        val yearShortText = "${stringResource(R.string.year)} "+getYear(d1)
                        Text(text = yearShortText, fontSize = 14.sp, color = Color.Gray)
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
                        Text(text = stringResource(R.string.month))
                        val d1 = Date()
                        val monthShortText = getNameOfMonthByNumber(d1.month, LocalContext.current)+" "+ getYear(d1)
                        Text(text = monthShortText, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectRangeSubDialog(openDialog: MutableState<Boolean>, startDate: MutableState<Date?>, endDate: MutableState<Date?>) {
    val openStartDateDialog = remember { mutableStateOf(false) }
    val openEndDateDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        if(startDate.value==null || endDate.value==null) {
            startDate.value = getCurrentMonthDates().first
            endDate.value = getCurrentMonthDates().second
        }

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
                    Text(text = stringResource(R.string.select_range), fontWeight = FontWeight.Bold)
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
                            .clickable { openStartDateDialog.value=true }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.from),
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
                        Text(text = getShortDateString(startDate.value!!, LocalContext.current), fontSize = 14.sp, color = Color.Gray)
                    }
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight())
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clickable { openEndDateDialog.value=true }
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.to),
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
                        Text(text = getShortDateString(endDate.value!!, LocalContext.current), fontSize = 14.sp, color = Color.Gray)
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
                        Text(text = stringResource(R.string.ok), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

        }

        StartDatePickerDialog(openDialog = openStartDateDialog, startDate = startDate)
        EndDatePickerDialog(openDialog = openEndDateDialog, endDate = endDate)
    }      
}


@Composable
fun DatePickerDialog(openDialog: MutableState<Boolean>, startDate: MutableState<Date?>, endDate: MutableState<Date?> = remember { mutableStateOf(Date())} ) {
    if(openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            var year = Date().year
            var month = Date().month
            var date = Date().date
            Column(modifier = Modifier.background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                AndroidView(
                    { CalendarView(it) },
                    modifier = Modifier
                        .wrapContentWidth(),
                    update = { views ->
                        views.setOnDateChangeListener { _, _year, _month, _date ->
                            year = _year - 1900
                            month = _month
                            date = _date
                        }
                    }
                )
                Button(onClick = {
                    pickDate(Date(year, month, date),startDate, endDate)
                    openDialog.value = false
                },
                    shape = RoundedCornerShape(30.dp)) {
                    Text(text = stringResource(R.string.ok), color = Color.White)
                }
            }

        }
    }
}

@Composable
fun StartDatePickerDialog(openDialog: MutableState<Boolean>, startDate: MutableState<Date?>) {
    if(openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            var year = if(startDate.value!=null) startDate.value!!.year else Date().year
            var month = if(startDate.value!=null) startDate.value!!.month else Date().month
            var date =if(startDate.value!=null) startDate.value!!.date else  Date().date
            Column(modifier = Modifier.background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                AndroidView(
                    { CalendarView(it) },
                    modifier = Modifier
                        .wrapContentWidth(),
                    update = { views ->
                        views.setOnDateChangeListener { _, _year, _month, _date ->
                            year = _year - 1900
                            month = _month
                            date = _date
                        }
                    }
                )
                Button(onClick = {
                    pickStartDate(Date(year, month, date), startDate)
                    openDialog.value = false
                },
                    shape = RoundedCornerShape(30.dp)) {
                    Text(text = stringResource(R.string.ok), color = Color.White)
                }
            }

        }
    }
}

@Composable
fun EndDatePickerDialog(openDialog: MutableState<Boolean>, endDate: MutableState<Date?>) {
    if(openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            var year = if(endDate.value!=null) endDate.value!!.year else Date().year
            var month = if(endDate.value!=null) endDate.value!!.month else Date().month
            var date =if(endDate.value!=null) endDate.value!!.date else  Date().date
            Column(modifier = Modifier.background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                AndroidView(
                    { CalendarView(it) },
                    modifier = Modifier
                        .wrapContentWidth(),
                    update = { views ->
                        views.setOnDateChangeListener { _, _year, _month, _date ->
                            year = _year - 1900
                            month = _month
                            date = _date
                        }
                    }
                )
                Button(onClick = {
                    pickEndDate(Date(year, month, date), endDate)
                    openDialog.value = false
                },
                    shape = RoundedCornerShape(30.dp)) {
                    Text(text = stringResource(R.string.ok), color = Color.White)
                }
            }

        }
    }
}

private fun pickStartDate(setDate: Date, startDate: MutableState<Date?>) {
    startDate.value = Date(setDate.year, setDate.month, setDate.date)
    startDate.value!!.hours = 0
    startDate.value!!.minutes = 0
    startDate.value!!.seconds = 0
}

private fun pickEndDate(setDate: Date, endDate: MutableState<Date?>) {
    endDate.value = Date(setDate.year, setDate.month, setDate.date)
    endDate.value!!.hours = 23
    endDate.value!!.minutes = 59
    endDate.value!!.seconds = 59
}

private fun pickDate(setDate: Date, startDate: MutableState<Date?>, endDate: MutableState<Date?>) {
    pickStartDate(setDate, startDate)
    pickEndDate(setDate, endDate)
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

fun getCurrentMonthDates(): Pair<Date, Date> {
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

fun getShortDateString(date: Date, context: Context): String {
    return date.date.toString() + " " + getMonthAbr(date, context) + " " + getYear(date)
}

private fun getShortDateRangeString(startDate: Date, endDate: Date, context: Context): String {
    if(startDate.year==endDate.year&&startDate.month==endDate.month&&startDate.date==endDate.date) {
        return getShortDateString(startDate, context)
    }
    return getShortDateString(startDate, context)+" - "+ getShortDateString(endDate, context)
}

private fun getMonthAbr(date: Date, context: Context): String {
    return getNameOfMonthByNumber(date.month, context).take(3);
}

private fun swapDates(startDate: MutableState<Date?>, endDate: MutableState<Date?>, swapRight: Boolean = true) {
    if(startDate.value==null || endDate.value==null) return

    val c1 = Calendar.getInstance()
    c1.time = startDate.value!!
    val c1Year = c1.get(Calendar.YEAR)
    val c1Month = c1.get(Calendar.MONTH)
    val c1Date =c1.get(Calendar.DAY_OF_MONTH)

    val c2 = Calendar.getInstance()
    c2.time = endDate.value!!
    val c2Year = c2.get(Calendar.YEAR)
    val c2Month = c2.get(Calendar.MONTH)
    val c2Date =c2.get(Calendar.DAY_OF_MONTH)

    var isSwapped = false

    if(c1Year==c2Year) {
        if(c1Month==c2Month) {
            if(c1Date==c2Date) {
                // Add one day
                if (swapRight) {
                    c1.add(Calendar.DATE, 1)
                    c2.add(Calendar.DATE, 1)
                } else {
                    c1.add(Calendar.DATE, -1)
                    c2.add(Calendar.DATE, -1)
                }
                isSwapped=true
            } else if(c1Date==1 && c2Date==c1.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                // Add one month
                if (swapRight) {
                    c1.add(Calendar.DATE, c1.getActualMaximum(Calendar.DAY_OF_MONTH)) //add max of this month
                    c2.add(Calendar.DATE, c1.getActualMaximum(Calendar.DAY_OF_MONTH)) //add max of next month
                } else {
                    c1.add(Calendar.MONTH, -1) //add max of this month
                    c2.time = c1.time
                    c2.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH))
                }

                isSwapped=true
            }
        } else if(c1Month==0&&c1Date==1&&c2Month==11&&c2Date==31) {
            // Add one year
            if (swapRight) {
                c1.add(Calendar.YEAR, 1)
                c2.add(Calendar.YEAR, 1)
            } else {
                c1.add(Calendar.YEAR, -1)
                c2.add(Calendar.YEAR, -1)
            }

            isSwapped=true
        }
    }

    if(!isSwapped) {
        val diff = getDifferenceBetweenCalendarDates(c1, c2)
        if (swapRight) {
            c1.add(Calendar.DATE, (diff+1))
            c2.add(Calendar.DATE, (diff+1))
        } else {
            c1.add(Calendar.DATE, -(diff+1))
            c2.add(Calendar.DATE, -(diff+1))
        }

    }

    pickStartDate(c1.time, startDate)
    pickEndDate(c2.time, endDate)
}

private fun getDifferenceBetweenCalendarDates(c1: Calendar, c2: Calendar): Int {
    val millionSeconds = c2.timeInMillis - c1.timeInMillis
    return TimeUnit.MILLISECONDS.toDays(millionSeconds).toInt()
}
