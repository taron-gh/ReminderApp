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
import java.util.*

class NotificationService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskId  = intent?.getLongExtra("id", 0)
        Log.i("Service", taskId.toString())
        val job = GlobalScope.launch {
            withContext(Dispatchers.IO){
                TaskManager.taskHit(taskId!!)
            }
        }
        Log.i("Service", "1")
        if (intent?.action == "done") {
            GlobalScope.launch {
                job.join()
                withContext(Dispatchers.IO){
                    if(TaskManager.getTask(taskId!!).postponed){
                        TaskManager.taskPostponedDone(taskId)
                    }else{
                        TaskManager.taskCompletelyDone(taskId)
                    }
                    stopSelf()
                }
            }
            Log.i("Service", "2")
        }else if(intent?.action == "postpone"){
            GlobalScope.launch {
                job.join()
                withContext(Dispatchers.IO){
                    TaskManager.taskPostpone(taskId!!)
                    stopSelf()
                }
            }
            Log.i("Service", "3")
        }else if(intent?.action == "cancel"){

            GlobalScope.launch {
                job.join()
                withContext(Dispatchers.IO){
                    if(TaskManager.getTask(taskId!!).postponed){
                        TaskManager.taskPostponedCancel(taskId)
                    }else{
                        TaskManager.taskCompletelyCancel(taskId)
                    }
                    stopSelf()
                }
            }
            Log.i("Service", "4")
        }
        else {
            val doneIntent = Intent(this, NotificationService::class.java).apply {
                action = "done"
                putExtra("id", taskId)
            }
            val postponeIntent = Intent(this, NotificationService::class.java).apply {
                action = "postpone"
                putExtra("id", taskId)
            }
            val cancelIntent = Intent(this, NotificationService::class.java).apply {
                action = "cancel"
                putExtra("id", taskId)
            }
            val donePendingIntent =
                PendingIntent.getService(this, 0, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val postponePendingIntent =
                PendingIntent.getService(this, 0, postponeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val cancelPendingIntent =
                PendingIntent.getService(this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val context = this
            GlobalScope.launch {
                val task = TaskManager.getTask(taskId!!)
                val description: String
                if(task.description.isBlank()){
                    description = "This task has no description."
                }else{
                    description = task.description
                }
                val notification = NotificationCompat.Builder(context, CHANNEL_DEFAULT)
                    .setContentTitle(task.name + " (" + task.category + ")")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentText(description)
                    .addAction(0, "Done!", donePendingIntent)
                    .addAction(0, "Postpone", postponePendingIntent)
                    .addAction(0, "Cancel", cancelPendingIntent)
                    .build()
                Log.i("Service", "5")
                startForeground(123, notification)
            }

        }
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null

    }
}