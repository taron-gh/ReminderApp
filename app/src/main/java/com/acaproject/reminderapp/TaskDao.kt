package com.acaproject.reminderapp

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    suspend fun getAllTasks(): List<Task>

//    @Transaction
//    @Query("SELECT * FROM task_table WHERE dayOfWeek = :day")
//    suspend fun getTodayTasks(day: Int): List<Task>

    @Transaction
    @Query("SELECT * FROM task_table WHERE category = :category")
    suspend fun getTasks(category: String): List<Task>

    @Transaction
    @Query("SELECT * FROM task_table WHERE taskId = :id")
    suspend fun getTask(id: Long): Task

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun removeTask(task: Task)
}