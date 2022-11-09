package com.andriyilliaandroidgeeks.moneysaver.presentation.categories.additional_composes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.ma.charts.pie.PieChart


@Composable
fun PieSimpleScreen() {
    ScreenContainer {
        items(PieSampleData) {
            ChartContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .animateContentSize(),
                title = "Pie - Legend::${it.legendPosition.name}"
            ) {
                PieChart(data = it)
            }
        }
    }
}