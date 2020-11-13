package com.acaproject.reminderapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.IllegalStateException

class HomeFragment : Fragment(), OnTaskClickListener {

    private lateinit var fragmentControl: FragmentControl

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentControl) {
            fragmentControl = context
        } else throw IllegalStateException("Activity must implement fragmentControl")


    }


    private lateinit var taskAdapter: TaskRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addDataSet()
        initRecyclerView()

        bottomNavBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        fragmentControl.updateToolBar("Home", false)
    }

    private fun initRecyclerView() {

        recyclerView.apply {
            adapter = taskAdapter
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            layoutManager = LinearLayoutManager(activity)

        }
    }

    override fun onItemClick(task: Task) {

        val builder = AlertDialog.Builder(activity)
        builder.apply {
            setTitle("Description")
            setMessage("${task.description}")
            setNeutralButton("Ok") { dialogInterface: DialogInterface, i: Int ->

            }
            setCancelable(false)
            show()
        }
    }

    private fun addDataSet() {
        val tasks = mutableListOf<Task>()
        val task = Task(111, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "2dsd", "xchj", 5, 4, true, false, 1)
        val task1 = Task(111, "aaa", "2dsd", "xchj", 5, 4, true, false, 1)
        val task2 = Task(111, "aaa", "2dsd", "xchj", 5, 4, true, false, 1)
        val task3 = Task(111, "aaa", "2dsd", "xchj", 5, 4, true, false, 1)
        val task4 = Task(111, "aaa", "2dsd", "xchj", 5, 4, true, false, 1)
        val task5 = Task(111, "aaa", "2dsd", "xchj", 5, 4, true, false, 1)
        tasks.add(task)
        tasks.add(task1)
        tasks.add(task2)
        tasks.add(task3)
        tasks.add(task4)
        tasks.add(task4)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)
        tasks.add(task5)

        taskAdapter = TaskRecyclerAdapter(tasks, this)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item->
        when(item.itemId){
            R.id.categoryMenu -> {
                Log.i("TAGFragment","category")
                val adapter = context?.let {
                    ArrayAdapter.createFromResource(
                        it,
                        R.array.Categories,
                        android.R.layout.simple_spinner_item
                    )
                }
                adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                return@OnNavigationItemSelectedListener true
            }
            R.id.weekMenu -> {
                Log.i("TAGFragment","week")
                val adapter = context?.let {
                    ArrayAdapter.createFromResource(
                        it,
                        R.array.Week,
                        android.R.layout.simple_spinner_item
                    )
                }
                adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                return@OnNavigationItemSelectedListener true
            }

        }

        false

    }


}