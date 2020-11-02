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
        Alarms.setAlarm(task.taskId, task.currentTime)
    }

    suspend fun removeTask(task: Task) {
        db.tasksDao().removeTask(task)
        Alarms.cancelAlarm(task.taskId)
        Alarms.cancelPostponedAlarm(task.taskId)
    }

    suspend fun updateTask(task: Task) {
        db.tasksDao().updateTask(task)
    }

    suspend fun updateTaskWithTime(task: Task){
        db.tasksDao().updateTask(task)
        Alarms.cancelAlarm(task.taskId)
        Alarms.cancelPostponedAlarm(task.taskId)
        Alarms.setAlarm(task.taskId, task.originalTime)
        if(task.taskState == TASK_POSTPONED){
            Alarms.setPostponedAlarm(task.taskId, task.originalTime)
        }
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

    suspend fun getTasks(stringId: Int): List<Task>? {
        return db.tasksDao().getTasks(context.getString(stringId));
    }

    suspend fun getTask(id: Long): Task{
        return db.tasksDao().getTask(id);
    }

    suspend fun taskCompletelyDone(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val calendar = Calendar.getInstance()
        //Alarms.cancelAlarm(id)
        calendar.timeInMillis = oldTask.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        newTask.currentTime = calendar.timeInMillis
        newTask.originalTime = calendar.timeInMillis
        newTask.taskState = TASK_COMPLETED
        newTask.postponed = false
        updateTaskWithTime(newTask)
    }

    suspend fun taskPostponedDone(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        newTask.currentTime = newTask.originalTime
        newTask.taskState = TASK_RUNNING
        newTask.postponed = false
        updateTaskWithTime(newTask)
    }

    suspend fun taskPostpone(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val currentCalendar = Calendar.getInstance()
        val originalCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = oldTask.currentTime
        originalCalendar.timeInMillis = oldTask.originalTime
        if(currentCalendar.get(Calendar.DAY_OF_YEAR) != originalCalendar.get(Calendar.DAY_OF_YEAR) &&
            Calendar.getInstance().compareTo(currentCalendar) > 0){
            Alarms.setPostponedAlarm(newTask.taskId, newTask.currentTime)
            newTask.taskState = TASK_POSTPONED
            newTask.postponed = true
            updateTaskWithTime(newTask)
        }else if(currentCalendar.get(Calendar.DAY_OF_YEAR) != originalCalendar.get(Calendar.DAY_OF_YEAR)){
            currentCalendar.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
            newTask.currentTime = currentCalendar.timeInMillis
            newTask.taskState = TASK_POSTPONED
            newTask.postponed = true
            updateTaskWithTime(newTask)
        }
    }

    suspend fun taskCompletelyCancel(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val calendar = Calendar.getInstance()
        //Alarms.cancelAlarm(id)
        calendar.timeInMillis = oldTask.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        newTask.currentTime = calendar.timeInMillis
        newTask.originalTime = calendar.timeInMillis
        newTask.taskState = TASK_COMPLETED
        newTask.postponed = false
        updateTaskWithTime(newTask)
    }

    suspend fun taskPostponedCancel(id: Long){
        val oldTask  = getTask(id)
        val newTask = oldTask
        val calendar = Calendar.getInstance()
        //Alarms.cancelAlarm(id)
        calendar.timeInMillis = oldTask.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        newTask.originalTime = calendar.timeInMillis
        newTask.currentTime = newTask.originalTime
        newTask.taskState = TASK_RUNNING
        newTask.postponed = false
        updateTaskWithTime(newTask)
    }

    suspend fun taskHit(id: Long) {
        val oldTask  = getTask(id)
        if(oldTask.repeatable){
            val newTask = oldTask
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = oldTask.originalTime
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            newTask.currentTime = calendar.timeInMillis
            newTask.originalTime = calendar.timeInMillis
            newTask.taskState = TASK_COMPLETED
            newTask.postponed = false
            updateTaskWithTime(newTask)
        }else{
            Alarms.cancelAlarm(oldTask.taskId)
            Alarms.cancelPostponedAlarm(oldTask.taskId)
        }


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
    var postponed: Boolean,
    var taskState: Int
)

