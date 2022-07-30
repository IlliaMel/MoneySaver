package com.example.moneysaver.presentation.categories

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.ui.theme.currencyColor
import com.example.moneysaver.ui.theme.currencyColorZero
import com.example.moneysaver.ui.theme.whiteSurface

@Composable
fun Categories(onNavigationIconClick: () -> Unit) {

    TopBarCategories(onNavigationIconClick = { onNavigationIconClick ()})
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)

        ) {

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(12.dp).border(BorderStroke(2.dp, Color.Red)),
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
                        .width(500.dp)
                        .height(500.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
                )
            }

            Column(modifier = Modifier.fillMaxHeight().fillMaxHeight().weight(0.8f), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
                CategoriesImage(CategoriesData.categoriesList.get(0))
                CategoriesImage(CategoriesData.categoriesList.get(2))
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).padding(12.dp).border(BorderStroke(2.dp, Color.Red)),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoriesData.categoriesList.forEach() {
                CategoriesImage(it)
            }
        }

        }
    }

}

@Composable
fun MyChartParent() {

}

@Composable
fun TopBarCategories(onNavigationIconClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.22f)
    ) {
        Image(
            painter = painterResource(R.drawable.bg5),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){

            Row(modifier = Modifier
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(modifier = Modifier
                    .padding(8.dp, 24.dp, 0.dp, 0.dp)
                    .size(40.dp, 40.dp), onClick = {onNavigationIconClick()}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = whiteSurface,
                        contentDescription = "Toggle drawer"
                    )
                }

                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier
                        .padding(0.dp, 12.dp, 0.dp, 4.dp) ,text = "Filter - Cash", color = whiteSurface, fontWeight = FontWeight.W300 , fontSize = 16.sp)
                    Text(text = ("3423 $"), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.accounts_name_label), color = whiteSurface, fontWeight = FontWeight.W500 , fontSize = 16.sp)
                    }
                }

                IconButton(modifier = Modifier
                    .padding(0.dp, 24.dp, 8.dp, 0.dp)
                    .size(40.dp, 40.dp), onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = whiteSurface,
                        contentDescription = "Toggle drawer"
                    )
                }

            }
        }
    }
    dividerForTopBar()
}

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