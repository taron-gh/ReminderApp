package com.acaproject.reminderapp

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object BackupConverter {
    private lateinit var context: Context
    private lateinit var importDirectory: File
    private lateinit var exportDirectory: File
    private const val ADD_TO_EXISTING = 1
    private const val DELETE_ALL = 2
    fun init(context1: Context){
        context = context1
        importDirectory = File(context.getExternalFilesDir(null), "Backup import directory")
        exportDirectory = File(context.getExternalFilesDir(null), "Backup export directory")
        importDirectory.mkdirs()
        exportDirectory.mkdirs()
    }

    fun createBackup(){
        GlobalScope.launch(Dispatchers.IO) {
            val tasksObject = FileList(TaskManager.getAllTasks())
            val gson = Gson()
            val dateString = Calendar.getInstance()
                .get(Calendar.DAY_OF_MONTH).toString() + "_" + (Calendar
                .getInstance().get(Calendar.MONTH) + 1).toString() + "_" + Calendar
                .getInstance().get(Calendar.YEAR).toString()
            val file = File(exportDirectory, "ReminderBackup__$dateString.rmdr")
            val fos = FileOutputStream(file)
            fos.use {
                fos.write(gson.toJson(tasksObject).toByteArray())
                fos.close()
            }
            withContext(Dispatchers.Main){
                Toast.makeText(context, "Backup created", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun readFromBackup(mode: Int, file: File){
        GlobalScope.launch(Dispatchers.IO){
            val length = file.length().toInt()
            val gson = Gson()
            val bytes = ByteArray(length)
            val fin = FileInputStream(file)
            fin.use {
                fin.read(bytes)
                fin.close()
            }
            val contents = String(bytes)
            val tasks = gson.fromJson(contents, FileList::class.java).list
            val finalTasksList: MutableList<Task> = arrayListOf()
            for(task in tasks){
                task.currentTime = task.originalTime
                val lastCalendar = Calendar.getInstance()
                lastCalendar.timeInMillis = task.originalTime
                if(task.originalTime < Calendar.getInstance().timeInMillis){
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = task.originalTime
                    calendar.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
                    while(calendar.get(Calendar.DAY_OF_WEEK) != lastCalendar.get(Calendar.DAY_OF_WEEK)){
                        calendar.add(Calendar.DAY_OF_WEEK, 1)
                    }
                    task.originalTime = calendar.timeInMillis
                    task.currentTime = calendar.timeInMillis
                    finalTasksList.add(task)
                }
            }
            if(mode == DELETE_ALL){
                TaskManager.deleteAllTasks()
            }
            for(task in finalTasksList){
                TaskManager.insertTask(task)
            }
        }


    }

    fun checkOnStart(context1: Context){
        if(importDirectory.listFiles().isNotEmpty()){
            if(importDirectory.listFiles()!!.size > 1){
                val dialog = AlertDialog.Builder(context1)
                    .setMessage("There are multiple backup files in import directory. Please leave only 1, and restart application.")
                    .setCancelable(true)
                    .setPositiveButton("Ok", null)
                    .create()
                dialog.show()
            }else{
                val dialog = AlertDialog.Builder(context1)
                    .setMessage("There is a backup file available.")
                    .setCancelable(true)
                    .setPositiveButton("Add to existing data"){ _, _ ->
                        val file = importDirectory.listFiles()!![0]
                        readFromBackup(ADD_TO_EXISTING, file)
                    }
                    .setNegativeButton("Clear existing  data"){ _, _ ->
                        val file = importDirectory.listFiles()!![0]
                        readFromBackup(DELETE_ALL, file)
                    }
                    .setNeutralButton("Ignore", null)
                    .create()
                dialog.show()
            }
        }
    }
    data class FileList (val list: List<Task>)
}