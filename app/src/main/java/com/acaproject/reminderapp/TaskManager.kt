package com.acaproject.reminderapp

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

object TaskManager {
    private lateinit var context: Context
    fun init(context1: Context) {
        context = context1;
    }

    private val db by lazy {
        Room.databaseBuilder(
            context,
            Database::class.java, "all"
        ).build()
    }
    val TASK_COMPLETED = 1
    val TASK_RUNNING = 2
    val TASK_SAFE_TO_DELETE = 3
    val TASK_POSTPONED = 4

    //*************Tasks Database**************
    suspend fun insertTask(task: Task) {
        db.tasksDao().insertTask(task)
    }

    suspend fun removeTask(task: Task) {
        db.tasksDao().removeTask(task)
    }

    suspend fun updateTask(task: Task) {
        db.tasksDao().updateTask(task)
    }

    suspend fun getTodayTasks(): List<Task>? {
        return db.tasksDao().getTodayTasks(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    }

    suspend  fun getTasks(stringId: Int): List<Task>? {
        return db.tasksDao().getTasks(context.getString(stringId));
    }
    suspend fun getTask(id: Int): Task{
        return db.tasksDao().getTask(id);
    }

}

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long,
    var name: String,
    var category: String,
    var description: String,
    var hour: Int,
    var minute: Int,
    var dayOfWeek: Int,
    var repeatable: Boolean,
    var taskState: Int
)

