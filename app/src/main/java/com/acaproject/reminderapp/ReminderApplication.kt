package com.acaproject.reminderapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class ReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        TaskManager.init(this)
//        Alarms.init(this)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_DEFAULT,
//                "Service channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                description = "Channel for foreground services"
//            }
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
    }
}