package com.example.moneysaver.presentation._components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import androidx.compose.ui.window.Dialog
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.presentation.transactions.additional_composes.getNameOfMonthByNumber
import com.example.moneysaver.ui.theme.dividerColor
import com.example.moneysaver.ui.theme.whiteSurface
import java.util.*

@Composable
fun DateRangePicker() {
    val openChoseDateRangeDialog = remember { mutableStateOf(false) }
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
                contentDescription = "Prev Month"
            )
        }

        DateRangeDisplay(date = Date(2022, 7, 31), onClick = {openChoseDateRangeDialog.value=true});

        IconButton(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .size(40.dp, 40.dp), onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                tint = whiteSurface,
                contentDescription = "Next Month"
            )
        }
    }
    ChoseDateRangeDialog(openChoseDateRangeDialog)
}

@Composable
fun DateRangeDisplay(date: Date, onClick: ()->Unit) {
    val monthName = getNameOfMonthByNumber(date.month);

    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date.date.toString(),
            modifier= Modifier
                .padding(4.dp, 0.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White)
                .padding(1.dp, 0.dp, 0.dp, 0.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.Black
        )
        Text(
            text = monthName.uppercase() + " "+ date.year.toString(),
            modifier=Modifier.padding(4.dp, 0.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.White
        )
        Text(
            text = "▾",
            modifier=Modifier.padding(4.dp, 0.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color= Color.White
        )
    }

}

@Composable
private fun ChoseDateRangeDialog(openDialog: MutableState<Boolean>) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
            }
        ) {
            val currentDate = Date()

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
                        .height(IntrinsicSize.Min)
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