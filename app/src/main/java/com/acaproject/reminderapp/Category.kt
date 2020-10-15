package com.acaproject.reminderapp

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

class Category(){
    companion object{
        var context: Context? = null
        fun init(context1: Context){
            context = context1;
        }
        private val db by lazy {
            Room.databaseBuilder(
                context!!,
                Database::class.java, "all"
            ).build()

        }
        val TASK_COMPLETED = 1
        val TASK_RUNNING = 2
        val TASK_SAFE_TO_DELETE = 3
        val TASK_POSTPONED = 4
        var listOfCategories: List<Categories> = ArrayList()
        //*************Categories Database**************
        fun addCategory(id: Int){
            GlobalScope.launch {
                db.categoriesDao().insertCategory(Categories(1, context!!.getString(id)))
                listOfCategories = db.categoriesDao().getAllCategories()
            }
        }
        fun removeCategory(id: Int){
            GlobalScope.launch {
                db.categoriesDao().removeCategory(Categories(1, context!!.getString(id)))
                listOfCategories = db.categoriesDao().getAllCategories()
            }
        }
        //*************Tasks Database**************
        fun insertTask(task: Task){
            GlobalScope.launch {
                db.tasksDao().insertTask(task)
            }
        }
        fun removeTask(task: Task){
            GlobalScope.launch {
                db.tasksDao().removeTask(task)
            }
        }
        fun updateTask(task: Task){
            GlobalScope.launch {
                db.tasksDao().updateTask(task)
            }
        }
        fun getTodayTasks(): List<Task>?{
            var list: List<Task>? = null
            GlobalScope.launch {
                list = db.tasksDao().getTodayTasks(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
            return list;
        }
        fun getTasks(stringId: Int): List<Task>?{
            var list: List<Task>? = null
            GlobalScope.launch {
                list = db.tasksDao().getTasks(context!!.getString(stringId));
            }
            return list;
        }

    }
    @Entity(tableName = "task_table")
    data class Task(
        @PrimaryKey(autoGenerate = true)
        var taskId: Long,
        val name: String,
        val category: String,
        val description: String,
        val hour: Int,
        val minute: Int,
        val dayOfWeek: Int,
        val repeatable: Boolean,
        val taskState: Int
    )
    @Entity(tableName = "category_table")
    data class Categories(
        @PrimaryKey(autoGenerate = true)
        var categoryId: Long,
        var category: String
    )
}