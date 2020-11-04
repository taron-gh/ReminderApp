package com.acaproject.reminderapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalStateException

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

        toolBar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolBar?.setNavigationOnClickListener { activity?.onBackPressed() }
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onResume() {
        super.onResume()
        fragmentControl.updateToolBar("Settings", true)
    }

}