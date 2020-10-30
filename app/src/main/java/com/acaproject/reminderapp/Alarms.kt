package com.acaproject.reminderapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

object Alarms {
    private lateinit var context: Context
    private lateinit var alarmManager: AlarmManager
    fun init(context1: Context){
        context = context1
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    fun setAlarm(primaryKey: Long, timeInMillis: Long){
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
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

    fun cancelAlarm(primaryKey: Long){
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    fun restartAlarmsAfterReboot(tasks: MutableList<Task>){
        for(task in tasks){
            setAlarm(task.taskId, task.currentTime)
        }
    }
}