package com.example.moneysaver.data.data_base.category_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneysaver.data.data_base.Converters
import com.example.moneysaver.domain.category.Category


@Database(
    entities = [Category::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CategoryDataBase: RoomDatabase() {
    abstract val categoryDao: CategoryDao

    companion object {
        const val DATABASE_NAME = "categories_db"
    }
}

