package com.acaproject.reminderapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment() {
    private lateinit var fragmentControl: FragmentControl

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentControl) {
            fragmentControl = context
        } else throw IllegalStateException("Activity must implement fragmentControl")

        fragmentControl.updateToolBar("Help", true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onResume() {
        super.onResume()
        fragmentControl.updateToolBar("Help", true)
    }

}