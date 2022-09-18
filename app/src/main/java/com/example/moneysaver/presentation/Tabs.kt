package com.example.moneysaver.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.ui.theme.inactiveColor


@Composable
fun TabsForScreens(
    modifier: Modifier = Modifier,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val imageWithTexts = listOf(
        ImageWithText(
            image = painterResource(id = R.drawable.accounts_img),
            text = stringResource(R.string.accounts)
        ),
        ImageWithText(
            image = painterResource(id = R.drawable.categories_img),
            text = stringResource(R.string.categories)
        ),
        ImageWithText(
            image = painterResource(id = R.drawable.transactions_img),
            text = stringResource(R.string.transactions)
        )
    )


    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.White,
            contentColor = Color.White,
            modifier = modifier.shadow(elevation = 5.dp)
        ) {
            imageWithTexts.forEachIndexed { index, item ->
                Tab(selected = selectedTabIndex == index,
                    selectedContentColor = Color.Black,
                    unselectedContentColor = inactiveColor,
                    onClick = {
                        selectedTabIndex = index
                        onTabSelected(index)
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = item.image,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(35.dp)
                                .height(23.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                        )
                        Text(
                            modifier = Modifier
                                .padding(2.dp),
                            text = item.text,
                            fontSize = 12.sp,
                            color = if (selectedTabIndex == index) Color.Black else inactiveColor
                        )
                    }

                }
            }
        }
    }

}