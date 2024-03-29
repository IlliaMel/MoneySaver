package com.andriyilliaandroidgeeks.moneysaver.presentation.categories.additional_composes

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import hu.ma.charts.legend.data.LegendEntry
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
import kotlin.math.roundToInt


val SimpleColors = listOf(
    Color(149, 207, 115, 255),
    Color(204, 204, 59, 255),
    Color(88, 166, 196, 255),
    Color(199, 57, 57, 255),
    Color(84, 202, 95, 255),
    Color.Magenta,
    Color.Cyan,
    Color.Green,
    Color.Gray,
)

enum class s {
    Hidden;
}

fun valueToNormalFormat(value : Double) : String{
    var balance = value.toString().split(".")
    return balance[0] + "." + balance[1][0] + (if(balance[1].length > 1) balance[1][1] else "")
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

internal val PieSampleData = LegendPosition.values().map {
    PieChartData(
        entries = listOf(430f, 240f, 140f, 60f, 888f).mapIndexed { idx, value ->
            PieChartEntry(
                value = value,
                label = AnnotatedString("")
            )
        },
        legendPosition = it,
        colors = SimpleColors,
        legendShape = CircleShape,
    )
}


@Composable
internal fun buildValuePercentString(item: LegendEntry) = buildAnnotatedString {
    item.value?.let { value ->
        withStyle(
            style = MaterialTheme.typography.body2.toSpanStyle()
                .copy(color = MaterialTheme.colors.primary)
        ) {
            append(value.toInt().toString())
        }
        append(" ")
    }

    withStyle(
        style = MaterialTheme.typography.caption.toSpanStyle()
            .copy(color = MaterialTheme.colors.secondary)
    ) {
        val percentString = item.percent.roundToInt().toString()
        append("($percentString %)")
    }
}