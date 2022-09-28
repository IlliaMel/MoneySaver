package com.example.moneysaver.presentation._components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moneysaver.R
import com.example.moneysaver.ui.theme.bordersPrimaryColor
import com.example.moneysaver.ui.theme.bordersSecondaryColor
import com.example.moneysaver.ui.theme.textPrimaryColor


@Composable
fun dividerForTopBar(
    height : Dp = 4.dp,
    clip : Dp = 5.dp,
    shadow : Dp = 1.dp,
    /*
    startColor : Color = Color(218, 213, 213, 255),
    middleColor : Color = Color(247, 247, 247, 255),
    endColor : Color = Color(194, 194, 194, 255),
    */
    startColor : Color = bordersSecondaryColor.copy(alpha = 0.4f),
    middleColor : Color = bordersSecondaryColor.copy(alpha = 1f),
    endColor : Color = bordersSecondaryColor.copy(alpha = 0.6f)


    ){
    Box(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        startColor,
                        middleColor,
                        endColor
                    )
                )
            )
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(clip))
            .shadow(elevation = shadow , ambientColor = textPrimaryColor)
    )
}