package com.acaproject.reminderapp

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.add_task_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class FloatingFragment:Fragment() {

    private lateinit var fragmentControl: FragmentControl

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentControl) {
            fragmentControl = context
        } else throw IllegalStateException("Activity must implement fragmentControl")

        fragmentControl.updateToolBar("Add_Task", true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_task_page, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        task_cancelBtn.setOnClickListener {

        }

        task_okBtn.setOnClickListener {

           // Toast.makeText(context, "ok button clicked", Toast.LENGTH_SHORT).show()
            val radioButtonID: Int = task_week.checkedRadioButtonId

            var chosenDayOfWeek: Int = 0
            if(radioButtonID != -1 && task_spinner.selectedItem != null){
                when(radioButtonID){
                    R.id.monday -> chosenDayOfWeek = Calendar.MONDAY
                    R.id.tuesday -> chosenDayOfWeek = Calendar.TUESDAY
                    R.id.wednesday -> chosenDayOfWeek = Calendar.WEDNESDAY
                    R.id.thursday -> chosenDayOfWeek = Calendar.THURSDAY
                    R.id.friday -> chosenDayOfWeek = Calendar.FRIDAY
                    R.id.saturday -> chosenDayOfWeek = Calendar.SATURDAY
                    R.id.sunday -> chosenDayOfWeek = Calendar.SUNDAY
                }

                val calendar: Calendar = Calendar.getInstance()

                calendar.set(Calendar.HOUR_OF_DAY, task_timePicker.hour)
                calendar.set(Calendar.MINUTE, task_timePicker.minute)

                while(calendar.get(Calendar.DAY_OF_WEEK) != chosenDayOfWeek){
                    calendar.add(Calendar.DAY_OF_WEEK, 1)
                }

                val task = Task(
                    0,
                    name = task_name.text.toString(),
                    category = task_spinner.selectedItem.toString(),
                    description = task_description.text.toString(),
                    originalTime = calendar.timeInMillis,
                    currentTime = calendar.timeInMillis,
                    repeatable = task_checkBox.isChecked,
                    postponed = false,
                    taskState = TaskManager.TASK_RUNNING
                )



                if(task_description.text.toString().isBlank()){
                    val dialog: AlertDialog = AlertDialog.Builder(activity)
                        .setMessage("Do you want to add task without description?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Yes"
                        ) { _, _ ->
                            GlobalScope.launch {
                                TaskManager.insertTask(Task(
                                    0,
                                    name = task_name.text.toString(),
                                    category = task_spinner.selectedItem.toString(),
                                    description = task_description.text.toString(),
                                    originalTime = calendar.timeInMillis,
                                    currentTime = calendar.timeInMillis,
                                    repeatable = task_checkBox.isChecked,
                                    postponed = false,
                                    taskState = TaskManager.TASK_RUNNING
                                ))
                            }
                            fragmentControl.sendTask(task)
                            fragmentManager?.popBackStack()
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                    dialog.show()
                }else{
                    GlobalScope.launch(Dispatchers.IO) {
                        TaskManager.insertTask(task)
                    }
                    fragmentControl.sendTask(task)
                    fragmentManager?.popBackStack()
                }


            }else{
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

        }
    }
    override fun onResume() {
        super.onResume()
        fragmentControl.updateToolBar("Add Task", true)
    }


}