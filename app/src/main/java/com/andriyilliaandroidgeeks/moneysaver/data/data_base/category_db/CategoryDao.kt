package com.andriyilliaandroidgeeks.moneysaver.data.data_base.category_db

import androidx.room.*
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM `category`  ORDER BY creationDate")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM `category` WHERE isForSpendings = :isForSpendings  ORDER BY creationDate")
    fun getCategories(isForSpendings: Boolean): Flow<List<Category>>

    @Query("SELECT * FROM `category` WHERE uuid = :uuid")
    suspend fun getCategoryByUUID(uuid: UUID): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM `category`")
    suspend fun deleteAll()

}

