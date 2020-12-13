package com.acaproject.reminderapp.fragments

import android.R.attr.defaultValue
import android.R.attr.key
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
import androidx.fragment.app.Fragment
import com.acaproject.reminderapp.FragmentControl
import com.acaproject.reminderapp.R
import com.acaproject.reminderapp.Task
import com.acaproject.reminderapp.TaskManager
import kotlinx.android.synthetic.main.add_task_page.*
import java.util.*


class EditFragment(val task: Task) :Fragment() {
    private lateinit var fragmentControl: FragmentControl
    private var itemPosition: Int = 0
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentControl) {
            fragmentControl = context
        } else throw IllegalStateException("Activity must implement fragmentControl")

        fragmentControl.updateToolBar("Edit_Task", true)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            itemPosition = bundle.getInt("position", defaultValue)
        }
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


        task_name.setText(task.name)
        task_description.setText(task.description)
        task_checkBox.isChecked=task.repeatable
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.originalTime
        task_timePicker.minute=calendar.get(Calendar.MINUTE)
        task_timePicker.hour=calendar.get(Calendar.HOUR)

        val radioButtonID: Int = task_week.checkedRadioButtonId


        task_cancelBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        task_okBtn.setOnClickListener {

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

                val dayOfWeek: RadioButton = task_week.findViewById(radioButtonID)
                val calendar: Calendar = Calendar.getInstance()

                calendar.set(Calendar.HOUR_OF_DAY, task_timePicker.hour)
                calendar.set(Calendar.MINUTE, task_timePicker.minute)

                while(calendar.get(Calendar.DAY_OF_WEEK) != chosenDayOfWeek){
                    calendar.add(Calendar.DAY_OF_WEEK, 1)
                }

                val newTask = Task(
                    task.taskId,
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
                            fragmentControl.editTask(newTask, itemPosition)
                        }
                        .setNegativeButton("Cancel", null)
                        .create()

                }

                fragmentControl.editTask(newTask, itemPosition)
                fragmentManager?.popBackStack()


            }else{
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onResume() {
        super.onResume()
       fragmentControl.updateToolBar("Edit Task", true)
    }
}