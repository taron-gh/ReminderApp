package com.acaproject.reminderapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class OnBootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, AlarmRecoveryAfterRebootService::class.java))
        } else {
            context.startService(Intent(context, AlarmRecoveryAfterRebootService::class.java))
        }

    }
}