package com.example.moneysaver.presentation.accounts.additional_composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.ui.theme.currencyColorZero
import com.example.moneysaver.ui.theme.textPrimaryColor


@Composable
fun ChooseAccountElement(
    title: String,
    subTitle: String,
    img: Painter,
    onClickChooser: () -> Unit
) {
    Row(modifier = Modifier.padding(16.dp,4.dp,8.dp,4.dp).fillMaxWidth().clickable(onClick = (onClickChooser)), horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.width(140.dp) , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {

            Text(
                text = title,
                fontWeight = FontWeight.W400,
                color = textPrimaryColor,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
            Text(
                text = subTitle,
                fontWeight = FontWeight.W400,
                color = currencyColorZero,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }



        Image(
            painter = img,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(0.dp,8.dp,8.dp,8.dp)
                .width(45.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(corner = CornerSize(4.dp))),
            colorFilter = ColorFilter.tint(textPrimaryColor)
        )
    }

}
