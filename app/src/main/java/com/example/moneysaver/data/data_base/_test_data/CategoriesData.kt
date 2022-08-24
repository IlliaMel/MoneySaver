package com.example.moneysaver.data.data_base._test_data

import com.example.moneysaver.R
import com.example.moneysaver.domain.model.Category

object CategoriesData {

    val addCategory = Category(
        categoryImg = VectorImg(img = R.drawable.ic_plus_categ_icn),
        currencyType = "",
        title = "Add Category",
        spent = 0.0,
    )

    val defaultCategory = Category(
        categoryImg = VectorImg(img = R.drawable.ic_categ_icn_1),
        currencyType = "",
        title = "Add Category",
        spent = 0.0,
    )

        val categoryImges = mutableListOf(
        VectorImg(img = R.drawable.ic_categ_icn_1, ),
        VectorImg(img = R.drawable.ic_categ_icn_2, ),
        VectorImg(img = R.drawable.ic_categ_icn_3, ),
        VectorImg(img = R.drawable.ic_categ_icn_4, ),
        VectorImg(img = R.drawable.ic_categ_icn_5, ),
        VectorImg(img = R.drawable.ic_categ_icn_6, ),
        VectorImg(img = R.drawable.ic_categ_icn_1, ),
        VectorImg(img = R.drawable.ic_categ_icn_2, ),
        VectorImg(img = R.drawable.ic_categ_icn_3, ),
        VectorImg(img = R.drawable.ic_categ_icn_4, ),
    )

    var defaultCategoryVectorImg =  VectorImg(img = R.drawable.ic_categ_icn_1)




}