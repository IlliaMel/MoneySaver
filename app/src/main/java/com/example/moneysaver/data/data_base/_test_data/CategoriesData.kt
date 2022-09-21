package com.example.moneysaver.data.data_base._test_data

import com.example.moneysaver.R
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.presentation.MainActivity

object CategoriesData {

    var addCategory = Category(
        categoryImg = VectorImg(img = R.drawable.ic_plus_categ_icn),
        currencyType = Currency(),
        title = MainActivity.instance!!.getString(R.string.add_category),
        spent = 0.0,
    )

    var defaultCategory = Category(
        categoryImg = VectorImg(img = R.drawable.ic_categ_icn_1),
        currencyType = Currency(),
        title = MainActivity.instance!!.getString(R.string.add_category),
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

    fun update() {
        addCategory = Category(
            categoryImg = VectorImg(img = R.drawable.ic_plus_categ_icn),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.add_category),
            spent = 0.0,
        )
        defaultCategory = Category(
            categoryImg = VectorImg(img = R.drawable.ic_categ_icn_1),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.add_category),
            spent = 0.0,
        )
    }


}