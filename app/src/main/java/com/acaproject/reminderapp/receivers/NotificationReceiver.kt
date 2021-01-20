package com.acaproject.reminderapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(
                Intent(context, NotificationService::class.java).putExtra(
                    "id",
                    intent.getLongExtra("id", 0)
                )
            )
        } else {
            context.startService(
                Intent(context, NotificationService::class.java).putExtra(
                    "id",
                    intent.getLongExtra("id", 0)
                )
            )
        }
    }
}