package com.andriyilliaandroidgeeks.moneysaver.data.data_base.category_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andriyilliaandroidgeeks.moneysaver.data.data_base.Converters
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category


@Database(
    entities = [Category::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class CategoryDataBase: RoomDatabase() {
    abstract val categoryDao: CategoryDao

    companion object {
        const val DATABASE_NAME = "categories_db"
    }
}

