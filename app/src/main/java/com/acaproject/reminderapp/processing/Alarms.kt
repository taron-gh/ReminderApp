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
    var minutesBeforeAlarms: Int = 0
    fun init(context1: Context) {
        context = context1
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    fun setAlarm(task: Task) {
        val primaryKey = task.taskId
        var timeInMillis = task.originalTime
        val calendar = Calendar.getInstance()
        if(minutesBeforeAlarms != 0){
            calendar.timeInMillis = timeInMillis
            calendar.add(Calendar.MINUTE, 0 - 2 * minutesBeforeAlarms)
        }
        timeInMillis = calendar.timeInMillis
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        Log.i("Alarms", primaryKey.toString())
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    fun setPostponedAlarm(task: Task) {
        val primaryKey = task.taskId
        var timeInMillis = task.currentTime
        val calendar = Calendar.getInstance()
        if(minutesBeforeAlarms != 0){
            calendar.timeInMillis = timeInMillis
            calendar.add(Calendar.MINUTE, 0 - 2 * minutesBeforeAlarms)
        }
        timeInMillis = calendar.timeInMillis
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(task: Task) {
        val primaryKey = task.taskId
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    fun cancelPostponedAlarm(task: Task) {
        val primaryKey = task.taskId
        val intent: Intent = Intent(context, NotificationReceiver::class.java).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            putExtra("id", primaryKey)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    fun restartAlarmsAfterReboot(tasks: MutableList<Task>) {
        for (task in tasks) {
            val currentCalendar = Calendar.getInstance()
            val originalCalendar = Calendar.getInstance()
            currentCalendar.timeInMillis = task.currentTime
            val lastCurrentDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK)
            originalCalendar.timeInMillis = task.originalTime
            val lastOriginalDayOfWeek = originalCalendar.get(Calendar.DAY_OF_WEEK)
            if (originalCalendar.timeInMillis < Calendar.getInstance().timeInMillis) {
                while (!(originalCalendar.timeInMillis > Calendar.getInstance().timeInMillis
                            && originalCalendar.get(Calendar.DAY_OF_WEEK) == lastOriginalDayOfWeek)) {
                    originalCalendar.add(Calendar.DAY_OF_WEEK, 1)
                }
            }
            if (task.postponed && currentCalendar.timeInMillis < Calendar.getInstance().timeInMillis) {
                while (!(currentCalendar.timeInMillis > Calendar.getInstance().timeInMillis
                            && currentCalendar.get(Calendar.DAY_OF_WEEK) == lastCurrentDayOfWeek)) {
                    currentCalendar.add(Calendar.DAY_OF_WEEK, 1)
                }
                setPostponedAlarm(task)
            }
            setAlarm(task)
        }
    }
}