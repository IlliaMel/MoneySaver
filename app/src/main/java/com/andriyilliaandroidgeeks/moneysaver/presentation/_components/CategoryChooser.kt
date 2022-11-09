package com.andriyilliaandroidgeeks.moneysaver.presentation._components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.presentation.accounts.additional_composes.VectorIcon
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.backgroundSecondaryColor
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.textPrimaryColor

@Composable
fun CategoryChooser(selectedCategory: MutableState<Category?>, categories: List<Category>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundSecondaryColor)
            .height(250.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            items(categories) { it ->
                Column(
                    modifier = Modifier
                        .clickable {
                            selectedCategory.value = it
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VectorIcon(modifierIcn = Modifier.padding(8.dp),height = 50.dp , width = 50.dp, vectorImg = it.categoryImg, onClick = {selectedCategory.value = it}, cornerSize = 50.dp)
                    Text(text = it.title, color = textPrimaryColor, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}


