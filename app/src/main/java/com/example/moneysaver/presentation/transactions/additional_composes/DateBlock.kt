package com.example.moneysaver.presentation.transactions.additional_composes

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.presentation.MainActivity
import com.example.moneysaver.ui.theme.currencyColor
import com.example.moneysaver.ui.theme.currencyColorSpent
import com.example.moneysaver.ui.theme.currencyColorZero
import com.example.moneysaver.ui.theme.gray
import java.util.*

@Composable
fun DateBlock(baseCurrency: Currency,  date: Date, balanceChange: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffececec))
            .innerShadow(color = Color(0xFF4D4D4D), blur = 0.5.dp, spread = 0.1.dp, drawLeft = false, drawRight = false)
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = if(date.date.toString().toDouble() > 9 ) date.date.toString() else "0" + date.date.toString(), color = Color(0xff7d7d7d), fontSize = 26.sp, fontWeight = FontWeight.Bold);
            Column(
                modifier = Modifier
                    .padding(12.dp, 0.dp, 0.dp, 0.dp)
                    .offset(x = 0.dp, y = (-2).dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = getNameOfDayByDate(date, LocalContext.current).uppercase(), color=Color(0xffababab), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = getNameOfMonthByNumber(date.month, LocalContext.current).uppercase() + " "+getYear(date), color = Color(0xff7d7d7d), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val balanceChangeText: String = (if(balanceChange>0) "+" else (if(balanceChange < 0) "-" else ""))+ baseCurrency.currency + " " + balanceChange
            val balanceChangeColor = if(balanceChange>0) currencyColor else (if(balanceChange < 0) currencyColorSpent else currencyColorZero)
            Text(text = balanceChangeText, color = balanceChangeColor, fontSize = 20.sp , fontWeight = FontWeight.W500)
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

fun getNameOfMonthByNumber(monthNumber: Int, context: Context): String {
    return when(monthNumber) {
        0 -> context.resources.getString(R.string.january)
        1 -> context.resources.getString(R.string.february)
        2 -> context.resources.getString(R.string.march)
        3 -> context.resources.getString(R.string.april)
        4 -> context.resources.getString(R.string.may)
        5 -> context.resources.getString(R.string.june)
        6 -> context.resources.getString(R.string.july)
        7 -> context.resources.getString(R.string.august)
        8 -> context.resources.getString(R.string.september)
        9 -> context.resources.getString(R.string.october)
        10-> context.resources.getString(R.string.november)
        else -> context.resources.getString(R.string.december)
    }
}

fun getNameOfDayByDate(date: Date, context: Context): String {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return when(calendar[Calendar.DAY_OF_WEEK]) {
        1 -> MainActivity.instance!!.applicationContext.getString(R.string.sunday)
        2 -> context.resources.getString(R.string.monday)
        3 -> context.resources.getString(R.string.tuesday)
        4 -> context.resources.getString(R.string.wednesday)
        5 -> context.resources.getString(R.string.thursday)
        6 -> context.resources.getString(R.string.friday)
        else -> context.resources.getString(R.string.saturday)
    }
}
