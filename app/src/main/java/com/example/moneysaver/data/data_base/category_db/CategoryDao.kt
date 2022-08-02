package com.example.moneysaver.data.data_base.category_db

import androidx.room.*
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM `category`")
    fun getCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM `category`")
    suspend fun deleteAll()

}

