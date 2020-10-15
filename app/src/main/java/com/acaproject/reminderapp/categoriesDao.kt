package com.acaproject.reminderapp

import androidx.room.*

@Dao
interface categoriesDao {

    @Query("SELECT * FROM category_table")
    suspend fun getAllCategories(): List<Category.Categories>

    @Delete
    suspend fun removeCategory(category: Category.Categories)

    @Insert
    suspend fun insertCategory(category: Category.Categories)

}