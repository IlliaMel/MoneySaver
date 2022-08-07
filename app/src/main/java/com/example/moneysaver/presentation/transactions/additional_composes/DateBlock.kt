package com.example.moneysaver.presentation.transactions.additional_composes

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun DateBlock(date: Date, balanceChange: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffececec))
            .innerShadow(blur = 4.dp, drawLeft = false, drawRight = false)
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = date.date.toString(), color = Color(0xff7d7d7d), fontSize = 26.sp, fontWeight = FontWeight.Bold);
            Column(
                modifier = Modifier
                    .padding(12.dp, 0.dp, 0.dp, 0.dp)
                    .offset(x = 0.dp, y = (-2).dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = getNameOfDayByDate(date).uppercase(), color=Color(0xffababab), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = getNameOfMonthByNumber(date.month).uppercase() + " "+getYear(date), color = Color(0xff7d7d7d), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val balanceChangeText: String = (if(balanceChange>0) "+" else (if(balanceChange < 0) "-" else ""))+"$ "+balanceChange
            val balanceChangeColor = if(balanceChange>0) Color.Green else (if(balanceChange < 0) Color.Red else Color.Gray)
            Text(text = balanceChangeText, color = balanceChangeColor, fontSize = 20.sp)
        }
    }
}

fun Modifier.innerShadow(
    color: Color = Color.Black,
    cornersRadius: Dp = 0.dp,
    spread: Dp = 0.dp,
    blur: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    drawLeft: Boolean = true,
    drawTop: Boolean = true,
    drawRight: Boolean = true,
    drawBottom: Boolean = true
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    drawIntoCanvas {

        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)
        it.drawRoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black
        it.drawRoundRect(
            left = left + spread.toPx() / 2 - (if (drawLeft) 0f else blur.toPx()*10),
            top = top + spread.toPx() / 2  - (if (drawTop) 0f else blur.toPx()*10),
            right = right - spread.toPx() / 2 + (if (drawRight) 0f else blur.toPx()*10),
            bottom = bottom - spread.toPx() / 2 + (if (drawBottom) 0f else blur.toPx()*10),
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        frameworkPaint.xfermode = null
        frameworkPaint.maskFilter = null
    }
}

fun getYear(date: Date): Int{
    return date.year+1900
}

fun getNameOfMonthByNumber(monthNumber: Int): String {
    return when(monthNumber) {
        0 -> "January"
        1 -> "February"
        2 -> "March"
        3 -> "April"
        4 -> "May"
        5 -> "June"
        6 -> "July"
        7 -> "August"
        8 -> "September"
        9 -> "October"
        10-> "November"
        else -> "December"
    }
}

fun getNameOfDayByDate(date: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return when(calendar[Calendar.DAY_OF_WEEK]) {
        1 -> "Sunday"
        2 -> "Monday"
        3 -> "Tuesday"
        4 -> "Wednesday"
        5 -> "Thursday"
        6 -> "Friday"
        else -> "Saturday"
    }
}
