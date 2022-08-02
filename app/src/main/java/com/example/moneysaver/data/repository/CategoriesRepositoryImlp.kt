package com.example.moneysaver.data.repository

import com.example.moneysaver.data.data_base.category_db.CategoryDao
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.domain.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow


class CategoriesRepositoryImlp(
    private val dao: CategoryDao
) : CategoriesRepository {


    override suspend fun getCategories(): Flow<List<Category>> {
        return dao.getCategories()
    }

    override suspend fun insertCategory(category: Category) {
        return dao.insertCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category)
    }

    override suspend fun deleteAllCategories() {
        dao.deleteAll()
    }


}