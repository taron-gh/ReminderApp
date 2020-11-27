package com.acaproject.reminderapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.add_task_page.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmRecoveryAfterRebootService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            TaskManager.resetAlarms()
            TaskManager.insertTask(Task(
                0,
                name = "after reboot",
                category = "aa",
                description = "aaa",
                originalTime = 1,
                currentTime = 1,
                repeatable = false,
                postponed = false,
                taskState = TaskManager.TASK_RUNNING
            ))
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT)
            .setContentTitle("Restoring data after reboot")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        startForeground(321, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return  null
    }
}