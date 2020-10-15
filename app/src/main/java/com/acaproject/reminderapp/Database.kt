package com.acaproject.reminderapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Category.Task::class, Category.Categories::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun tasksDao(): TaskDao
    abstract fun categoriesDao(): categoriesDao
}