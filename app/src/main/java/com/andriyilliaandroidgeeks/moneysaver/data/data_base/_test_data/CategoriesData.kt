package com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data

import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivity

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
        VectorImg(img = R.drawable.ic_categ_icn_7, ),
        VectorImg(img = R.drawable.ic_categ_icn_8, ),
        VectorImg(img = R.drawable.ic_categ_icn_9, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_1, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_2, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_3, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_4, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_5, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_5, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_6, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_7, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_8, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_1, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_2, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_3, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_4, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_5, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_6, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_7, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_8, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_9, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_9_0, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_9_1, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_9_2, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_9_3, ),
        VectorImg(img = R.drawable.ic_categ_icn_9_9_9_4, ),
    )


    fun update() {
        addCategory = Category(
            categoryImg = VectorImg(img = R.drawable.ic_plus_categ_icn),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.add_category),
            spent = 0.0,
        )
        defaultCategory = Category(
            categoryImg = VectorImg(img = R.drawable.ic_categ_icn_9_7),
            currencyType = Currency(),
            title = MainActivity.instance!!.getString(R.string.add_category),
            spent = 0.0,
        )
    }


}