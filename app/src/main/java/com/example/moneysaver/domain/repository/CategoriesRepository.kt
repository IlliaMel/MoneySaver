package com.example.moneysaver.domain.repository

import androidx.room.Query
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import kotlinx.coroutines.flow.Flow
import java.util.*


interface CategoriesRepository {

    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryByUUID(uuid: UUID): Category?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun deleteAllCategories()

}