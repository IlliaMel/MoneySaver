package com.example.moneysaver.domain.repository

import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import kotlinx.coroutines.flow.Flow


interface CategoriesRepository {

     fun getCategories(): Flow<List<Category>>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun deleteAllCategories()

}