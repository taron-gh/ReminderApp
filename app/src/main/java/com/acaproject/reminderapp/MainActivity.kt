package com.acaproject.reminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val CHANNEL_DEFAULT = "channel"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 10)
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
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                TaskManager.insertTask(
                    Task(
                        0,
                        "a",
                        getString(R.string.hobby),
                        "description",
                        calendar.timeInMillis,
                        calendar.timeInMillis,
                        false,
                        false,
                        TaskManager.TASK_RUNNING
                    )
                )
            }

        }

    }

}