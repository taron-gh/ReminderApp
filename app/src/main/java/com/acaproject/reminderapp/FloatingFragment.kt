package com.acaproject.reminderapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.RequiresApi
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
        return inflater.inflate(R.layout.add_task_page,container,false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        task_okBtn.setOnClickListener {

            val radioButtonID: Int = task_week.checkedRadioButtonId
            val dayOfWeek: RadioButton = task_week.findViewById(radioButtonID)
            val calendar: Calendar = Calendar.getInstance()
//            calendar.set(Calendar.AM_PM, )
//            val task = Task(
//                0,
//                task_name.text.toString(),
//                task_spinner.selectedItem.toString(),
//                task_description.text.toString(),
//                task_timePicker.hour,
//                task_timePicker.minute,
//                dayOfWeek.text.toString(),
//                task_checkBox.isChecked,
//                1
//            )

            GlobalScope.launch (Dispatchers.IO){

            }
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentControl.updateToolBar("Add_Task", true)
    }
}