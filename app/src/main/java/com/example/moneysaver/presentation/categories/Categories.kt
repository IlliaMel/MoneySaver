package com.example.moneysaver.presentation.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.ui.theme.currencyColor
import com.example.moneysaver.ui.theme.currencyColorZero

@Composable
fun Categories() {

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(0.8f).padding(12.dp).border(BorderStroke(2.dp, Color.Red)),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoriesData.categoriesList.forEach() {
                CategoriesImage(it)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().weight(2f).border(BorderStroke(2.dp, Color.Red)),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center) {

            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(0.8f), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
                CategoriesImage(CategoriesData.categoriesList.get(0))
                CategoriesImage(CategoriesData.categoriesList.get(2))
            }

            Column(modifier = Modifier.fillMaxHeight().fillMaxWidth().weight(2f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = CategoriesData.categoriesList.get(0).categoryImg),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(4.dp)
                        .width(600.dp)
                        .height(600.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                )
            }

            Column(modifier = Modifier.fillMaxHeight().fillMaxHeight().weight(0.8f), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
                CategoriesImage(CategoriesData.categoriesList.get(0))
                CategoriesImage(CategoriesData.categoriesList.get(2))
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(0.8f).padding(12.dp).border(BorderStroke(2.dp, Color.Red)),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoriesData.categoriesList.forEach() {
                CategoriesImage(it)
            }
        }
    }




}
/*

    Text(text = account.title, fontWeight = FontWeight.W400 ,color = Color.Black , fontSize = 15.sp)
    Text(modifier = modifier.padding(0.dp, 2.dp, 0.dp, 0.dp) , text = (account.balance.toString() + " " + account.currencyType), color = currencyColor, fontWeight = FontWeight.W400 , fontSize = 15.sp)
 */

@Composable
private fun CategoriesImage(category: Category) {
    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(4.dp), fontSize = 13.sp, fontWeight = FontWeight.W400, text = category.title, color = Color.Black)
        Image(
            painter = painterResource(id = category.categoryImg),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(4.dp)
                .width(50.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
        )
        var color = currencyColor
        if(category.spended == 0.0)
            color = currencyColorZero

        Text(modifier = Modifier.padding(4.dp), fontSize = 13.sp, fontWeight = FontWeight.W500, text = (category.spended.toString() + " " + category.currencyType), color = color)
    }

}