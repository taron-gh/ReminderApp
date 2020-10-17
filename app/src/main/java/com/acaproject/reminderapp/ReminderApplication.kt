package com.acaproject.reminderapp

import android.app.Application

class ReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskManager.init(this)
    }
}