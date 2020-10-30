package com.acaproject.reminderapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 3)
abstract class Database : RoomDatabase() {
    abstract fun tasksDao(): TaskDao
}