package com.example.moneysaver.presentation.categories.additional_composes

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.ma.charts.ChartShape
import hu.ma.charts.bars.data.HorizontalBarsData
import hu.ma.charts.bars.data.StackedBarData
import hu.ma.charts.bars.data.StackedBarEntry
import hu.ma.charts.legend.data.LegendAlignment
import hu.ma.charts.legend.data.LegendEntry
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.line.data.AxisLabel
import hu.ma.charts.line.data.ChartColors
import hu.ma.charts.line.data.DrawAxis
import hu.ma.charts.line.data.LineChartData
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
import hu.ma.charts.table.data.TableEntry
import kotlin.math.roundToInt
import kotlin.random.Random

val Categories = listOf(
    "Teams",
    "Locations",
    "Devices",
    "People",
    "Laptops",
    "Titles",
    "Flowers",
    "Bugs",
    "Windows",
    "Screens",
    "Colors",
    "Bottles",
    "Cars",
    "Tricks",
)

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


internal val PieSampleData = LegendPosition.values().map {
    PieChartData(
        entries = listOf(430f, 240f, 140f, 60f, 888f).mapIndexed { idx, value ->
            PieChartEntry(
                value = value,
                label = AnnotatedString(Categories[idx])
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