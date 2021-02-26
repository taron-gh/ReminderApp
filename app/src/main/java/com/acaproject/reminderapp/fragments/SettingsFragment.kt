package com.acaproject.reminderapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.acaproject.reminderapp.Alarms
import com.acaproject.reminderapp.BackupConverter
import com.acaproject.reminderapp.FragmentControl
import com.acaproject.reminderapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.list_item.*


class SettingsFragment : Fragment() {

    private lateinit var fragmentControl: FragmentControl



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentControl) {
            fragmentControl = context

        } else throw IllegalStateException("Activity must implement fragmentControl")
        fragmentControl.updateToolBar("Settings", true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toolBar?.title
        toolBar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolBar?.setNavigationOnClickListener { activity?.onBackPressed() }

        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timeEditText.text =
            Editable.Factory.getInstance().newEditable(Alarms.minutesBeforeAlarms.toString())
        timeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.i("TAG", "text changed")
                if (s.toString() != "") {
                    Alarms.minutesBeforeAlarms = s.toString().toInt()
                    Log.i("TAG", "value changed to: " + Alarms.minutesBeforeAlarms)
                }
            }
        })
        exportButton.setOnClickListener { BackupConverter.createBackup() }

    }

    override fun onResume() {
        super.onResume()
        fragmentControl.updateToolBar("Settings", true)

    }

}