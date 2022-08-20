package com.example.moneysaver.data.data_base.test_data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.moneysaver.R
import com.example.moneysaver.domain.category.Category
import java.io.Serializable

object CategoriesData {

    val addCategory = Category(
        categoryImg = VectorImg(img = R.drawable.ic_plus_categ_icn),
        currencyType = "",
        title = "Add Category",
        spent = 0.0,
    )

    val categoryImges = mutableListOf(
        VectorImg(img = R.drawable.ic_categ_icn_1, externalColor = Color(244, 67, 54, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_2, externalColor = Color(233, 30, 99, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_3, externalColor = Color(156, 39, 176, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_4, externalColor = Color(103, 58, 183, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_5, externalColor = Color(63, 81, 181, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_6, externalColor = Color(33, 150, 243, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_1, externalColor = Color(0, 150, 136, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_2, externalColor = Color(76, 175, 80, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_3, externalColor = Color(255, 235, 59, 255)),
        VectorImg(img = R.drawable.ic_categ_icn_4, externalColor = Color(255, 87, 34, 255)),
    )

    var categoryAdder = Category(
        categoryImg = VectorImg(img = R.drawable.ic_plus_categ_icn),
        currencyType = "",
        title = "Add new",
        spent = 0.0,
    )

}