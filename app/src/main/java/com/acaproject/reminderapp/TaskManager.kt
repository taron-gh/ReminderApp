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
    suspend fun resetAlarms(){
        Alarms.restartAlarmsAfterReboot(db.tasksDao().getAllTasks() as MutableList<Task>)
    }
    suspend fun getTodayTasks(): List<Task>? {
        val returnList: MutableList<Task> = db.tasksDao().getAllTasks() as MutableList<Task>
        for(task in returnList){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.currentTime
            if(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
                returnList.remove(task)
            }
        }
        return returnList
    }

    suspend  fun getTasks(stringId: Int): List<Task>? {
        return db.tasksDao().getTasks(context.getString(stringId));
    }
    suspend fun getTask(id: Long): Task{
        return db.tasksDao().getTask(id);
    }

    suspend fun taskDone(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val calendar = Calendar.getInstance()
        //Alarms.cancelAlarm(id)
        calendar.timeInMillis = oldTask.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        newTask.currentTime = calendar.timeInMillis
        newTask.originalTime = calendar.timeInMillis
        newTask.taskState = TASK_COMPLETED
        updateTask(newTask)
    }

    suspend fun taskPostpone(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val currentCalendar = Calendar.getInstance()
        val originalCalendar = Calendar.getInstance()
        //Alarms.cancelAlarm(id)
        currentCalendar.timeInMillis = oldTask.currentTime
        originalCalendar.timeInMillis = oldTask.originalTime
        currentCalendar.add(Calendar.DAY_OF_YEAR, 1)
        if(currentCalendar.get(Calendar.DAY_OF_YEAR) != originalCalendar.get(Calendar.DAY_OF_YEAR)){
            newTask.currentTime = currentCalendar.timeInMillis
            newTask.taskState = TASK_POSTPONED
            updateTask(newTask)
        }
    }

    suspend fun taskCancel(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val calendar = Calendar.getInstance()
        //Alarms.cancelAlarm(id)
        calendar.timeInMillis = oldTask.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        newTask.currentTime = calendar.timeInMillis
        newTask.originalTime = calendar.timeInMillis
        newTask.taskState = TASK_COMPLETED
        updateTask(newTask)
    }
}

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long,
    var name: String,
    var category: String,
    var description: String,
    var originalTime: Long,
    var currentTime: Long,
    var repeatable: Boolean,
    var taskState: Int
)

