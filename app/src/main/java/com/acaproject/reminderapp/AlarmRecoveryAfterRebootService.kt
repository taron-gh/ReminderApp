package com.acaproject.reminderapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmRecoveryAfterRebootService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            TaskManager.resetAlarms()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return  null
    }
}