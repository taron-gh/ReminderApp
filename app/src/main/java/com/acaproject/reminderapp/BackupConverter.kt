package com.acaproject.reminderapp

import android.content.Context
import java.io.File

object BackupConverter {
    private lateinit var context: Context
    private lateinit var importDirectory: File
    private lateinit var exportDirectory: File
    fun init(context1: Context){
        context = context1
        importDirectory = File(context.getExternalFilesDir(null), "Backup import directory")
        exportDirectory = File(context.getExternalFilesDir(null), "Backup export directory")
        importDirectory.mkdirs()
        exportDirectory.mkdirs()
    }
    fun checkOnStart(){

    }
}