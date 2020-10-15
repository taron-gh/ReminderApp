package com.acaproject.reminderapp

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks(): List<Category.Task>

    @Transaction
    @Query("SELECT * FROM task_table WHERE dayOfWeek = :day")
    suspend fun getTodayTasks(day: Int): List<Category.Task>

    @Transaction
    @Query("SELECT * FROM task_table WHERE category = :category")
    suspend fun getTasks(category: String): List<Category.Task>

    @Insert
    suspend fun insertTask(task: Category.Task)

    @Update
    suspend fun updateTask(task: Category.Task)

    @Delete
    suspend fun removeTask(task: Category.Task)
}