package com.andriyilliaandroidgeeks.moneysaver.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.presentation._components.dividerForTopBar
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.backgroundPrimaryColor
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.inactiveColor
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.textPrimaryColor


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
            image = painterResource(id = R.drawable.ic_account_img_9_5),
            text = stringResource(R.string.accounts)
        ),
        ImageWithText(
            image = painterResource(id = R.drawable.ic_account_img_9_4),
            text = stringResource(R.string.categories)
        ),
        ImageWithText(
            image = painterResource(id = R.drawable.ic_account_img_9_0),
            text = stringResource(R.string.transactions)
        )
    )


    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        dividerForTopBar(height = 2.dp)
        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = backgroundPrimaryColor,
            contentColor = backgroundPrimaryColor,
            modifier = modifier.shadow(elevation = 6.dp, ambientColor = textPrimaryColor)
        ) {
            imageWithTexts.forEachIndexed { index, item ->
                Tab(selected = selectedTabIndex == index,
                    selectedContentColor = textPrimaryColor,
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
                                .padding(2.dp,4.dp,2.dp,0.dp)
                                .width(38.dp)
                                .height(38.dp),
                            colorFilter = ColorFilter.tint(textPrimaryColor)
                        )
                        Text(
                            modifier = Modifier
                                .padding(2.dp,0.dp,2.dp,2.dp),
                            text = item.text,
                            fontSize = 11.sp,
                            color = if (selectedTabIndex == index) textPrimaryColor else inactiveColor
                        )
                    }

                }
            }
        }
    }

}