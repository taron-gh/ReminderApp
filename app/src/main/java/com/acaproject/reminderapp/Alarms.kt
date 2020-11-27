package com.acaproject.reminderapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

object Alarms {
    private lateinit var context: Context
    private lateinit var alarmManager: AlarmManager
    fun init(context1: Context){
        context = context1
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    fun setAlarm(task: Task){
        val primaryKey = task.taskId
        val timeInMillis = task.originalTime
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        Log.i("Alarms", primaryKey.toString())
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }else{
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    fun setPostponedAlarm(task: Task){
        val primaryKey = task.taskId
        val timeInMillis = task.currentTime
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }else{
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(task: Task){
        val primaryKey = task.taskId
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    fun cancelPostponedAlarm(task: Task){
        val primaryKey = task.taskId
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    fun restartAlarmsAfterReboot(tasks: MutableList<Task>){
        for(task in tasks){
            setAlarm(task)
            if(task.postponed){
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = task.currentTime
                if(Calendar.getInstance().compareTo(calendar) > 0){
                    setPostponedAlarm(task)
                }else{
                    calendar.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
                    task.currentTime = calendar.timeInMillis
                    GlobalScope.launch {
                        TaskManager.updateTaskWithTime(task)
                    }
                }
            }
        }
    }
}