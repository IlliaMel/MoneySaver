package com.example.moneysaver.presentation._components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.presentation.accounts.additional_composes.VectorIcon

@Composable
fun CategoryChooser(selectedCategory: MutableState<Category?>, categories: List<Category>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xffeeeeee))
            .height(300.dp),
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
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VectorIcon(height = 60.dp , width = 60.dp, vectorImg = it.categoryImg, onClick = {selectedCategory.value = it}, cornerSize = 50.dp)
                    Text(text = it.title, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}


