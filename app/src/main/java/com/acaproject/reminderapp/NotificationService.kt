package com.acaproject.reminderapp

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class NotificationService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskId  = intent?.getIntExtra("id", 0)
        Log.i("Service", "onStartCommand")
        if (intent?.action == "done") {
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val oldTask  = TaskManager.getTask(taskId!!)
                    val newTask = oldTask
                    newTask.taskState = TaskManager.TASK_COMPLETED
                    TaskManager.updateTask(newTask)
                    stopSelf()
                }
            }
        }else if(intent?.action == "postpone"){
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val oldTask  = TaskManager.getTask(taskId!!)
                    val newTask = oldTask
                    newTask.dayOfWeek++
                    TaskManager.updateTask(newTask)
                    stopSelf()
                }
            }
        }else if(intent?.action == "cancel"){
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val oldTask  = TaskManager.getTask(taskId!!)
                    val newTask = oldTask
                    newTask.dayOfWeek += 7;
                    TaskManager.updateTask(newTask)
                    stopSelf()
                }
            }
        }
        else {
            val doneIntent = Intent(this, NotificationService::class.java).apply {
                action = "done"
            }
            val postponeIntent = Intent(this, NotificationService::class.java).apply {
                action = "postpone"
            }
            val cancelIntent = Intent(this, NotificationService::class.java).apply {
                action = "cancel"
            }
            val donePendingIntent =
                PendingIntent.getService(this, 0, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val postponePendingIntent =
                PendingIntent.getService(this, 0, postponeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val cancelPendingIntent =
                PendingIntent.getService(this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT)
                .setContentTitle("Wake up")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(0, "Done!", donePendingIntent)
                .addAction(0, "Postpone", postponePendingIntent)
                .addAction(0, "Cancel", cancelPendingIntent)
                .build()

            startForeground(123, notification)
        }
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null

    }
}