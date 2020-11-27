package com.acaproject.reminderapp

import android.content.Context
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import java.util.*

object TaskManager {
    private lateinit var context: Context
    private lateinit var db: Database
    fun init(context1: Context) {
        context = context1
        db = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java
        ).build()
    }
    const val TASK_COMPLETED = 1
    const val TASK_RUNNING = 2
    const val TASK_SAFE_TO_DELETE = 3
    const val TASK_POSTPONED = 4

    //*************Tasks Database**************
    suspend fun insertTask(task: Task) {
        Log.i("TAG", "1")
        val id = db.tasksDao().insertTask(task)
        Log.i("TAG", "2")
        task.taskId = id;
        Alarms.setAlarm(task)
        Log.i("TAG", "3")
    }

    suspend fun removeTask(task: Task) {
        db.tasksDao().removeTask(task)
        Alarms.cancelAlarm(task)
        Alarms.cancelPostponedAlarm(task)
    }

    suspend fun updateTask(task: Task) {
        db.tasksDao().updateTask(task)
    }

    suspend fun updateTaskWithTime(task: Task){
        db.tasksDao().updateTask(task)
        Alarms.cancelAlarm(task)
        Alarms.cancelPostponedAlarm(task)
        Alarms.setAlarm(task)
        if(task.postponed){
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = task.currentTime
            if(Calendar.getInstance().compareTo(calendar) < 0){
                calendar.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
                task.currentTime = calendar.timeInMillis

            }
            Alarms.setPostponedAlarm(task)
        }
    }

    suspend fun resetAlarms(){
        Alarms.restartAlarmsAfterReboot(db.tasksDao().getAllTasks() as MutableList<Task>)
    }
    suspend fun  getTaskByDayOfWeek(dayOfWeek: Int) : List<Task>?{
        val returnList: MutableList<Task> = db.tasksDao().getAllTasks() as MutableList<Task>
        for(task in returnList){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.originalTime
            if(calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek){
                returnList.remove(task)
            }
        }
        return returnList
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
        return db.tasksDao().getTasks(context.getString(stringId))
    }

    suspend fun getTask(id: Long): Task{
        return db.tasksDao().getTask(id)
    }

    suspend fun taskCompletelyDone(id: Long){
        val task  = getTask(id)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        task.currentTime = calendar.timeInMillis
        task.originalTime = calendar.timeInMillis
        task.taskState = TASK_COMPLETED
        task.postponed = false
        updateTaskWithTime(task)
    }

    suspend fun taskPostponedDone(id: Long){
        val task  = getTask(id)
        task.currentTime = task.originalTime
        task.taskState = TASK_RUNNING
        task.postponed = false
        updateTaskWithTime(task)
    }

    suspend fun taskPostpone(id: Long){
        val task  = getTask(id)
        val currentCalendar = Calendar.getInstance()
        val originalCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = task.currentTime
        originalCalendar.timeInMillis = task.originalTime
        currentCalendar.add(Calendar.DAY_OF_WEEK, 1)
        Log.i("TAG", "postponed 1")
        if(currentCalendar.get(Calendar.DAY_OF_WEEK) != originalCalendar.get(Calendar.DAY_OF_WEEK)){
            Log.i("TAG", "postponed 2")
            task.currentTime = currentCalendar.timeInMillis
            task.taskState = TASK_POSTPONED
            task.postponed = true
            updateTask(task)
            Alarms.setPostponedAlarm(task)
        }
    }

    suspend fun taskCompletelyCancel(id: Long){
        val task  = getTask(id)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        task.currentTime = calendar.timeInMillis
        task.originalTime = calendar.timeInMillis
        task.taskState = TASK_COMPLETED
        task.postponed = false
        updateTaskWithTime(task)
    }

    suspend fun taskPostponedCancel(id: Long){
        val task  = getTask(id)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.originalTime
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        task.originalTime = calendar.timeInMillis
        task.currentTime = task.originalTime
        task.taskState = TASK_RUNNING
        task.postponed = false
        updateTaskWithTime(task)
    }

    suspend fun taskHit(id: Long) {
        val task  = getTask(id)
        if(task.repeatable){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.originalTime
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            task.currentTime = calendar.timeInMillis
            task.originalTime = calendar.timeInMillis
            task.taskState = TASK_COMPLETED
            task.postponed = false
            updateTaskWithTime(task)
        }else{
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.originalTime
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            task.currentTime = calendar.timeInMillis
            task.originalTime = calendar.timeInMillis
            task.taskState = TASK_COMPLETED
            Alarms.cancelAlarm(task)
            Alarms.cancelPostponedAlarm(task)
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

