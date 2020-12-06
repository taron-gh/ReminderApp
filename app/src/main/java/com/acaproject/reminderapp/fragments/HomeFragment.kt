package com.acaproject.reminderapp.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.acaproject.reminderapp.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*


class HomeFragment : Fragment(), OnTaskClickListener {

    private lateinit var fragmentControl: FragmentControl
    private lateinit var addedTask: Task
    private val tasks = mutableListOf<Task>()

    private val categorySpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            val filteredTasks = tasks.filter {
                it.category == parent?.getItemAtPosition(position) .toString()
            }
                    taskAdapter.updateList(filteredTasks)
        }
    }


    private val weekSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        }

    }

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
        initSpinners()

        bottomNavBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        super.onViewCreated(view, savedInstanceState)
    }

   private fun initSpinners(){
        val adapterCategory = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Categories,
                android.R.layout.simple_spinner_item
            )
        }

        adapterCategory?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_category.adapter = adapterCategory
        spinner_category.onItemSelectedListener = categorySpinnerListener

        val adapterWeek = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Week,
                android.R.layout.simple_spinner_item
            )
        }


        adapterWeek?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_week.adapter = adapterWeek
        spinner_week.onItemSelectedListener = weekSpinnerListener

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
            setMessage(task.description)
            setNeutralButton("Ok", null)
            setCancelable(false)
            show()
        }
    }

    override fun onItemLongClick(task: Task) {
        val builder = AlertDialog.Builder(activity)
        builder.apply {
            setMessage("Are you sure you want to delete this task?")
            setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                GlobalScope.launch { TaskManager.removeTask(task) }
            }
            setNeutralButton("Cancel", null)
            setCancelable(false)
            show()
        }
    }

    override fun editTaskPage(task: Task) {
        editBtn.setOnClickListener {

            fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val editFragment = EditFragment(task)
            fragmentControl.openPage("Edit Task", true, editFragment)

        }
    }


    private fun addDataSet() {

        taskAdapter = TaskRecyclerAdapter(tasks, this)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.categoryMenu -> {

                    spinner_week.visibility = View.GONE
                    spinner_category.visibility = View.VISIBLE

                    return@OnNavigationItemSelectedListener true
                }
                R.id.weekMenu -> {

                    spinner_category.visibility = View.GONE
                    spinner_week.visibility = View.VISIBLE

                    return@OnNavigationItemSelectedListener true
                }

            }

            false

        }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun edit(task:Task){
        GlobalScope.launch (Dispatchers.IO){
            TaskManager.updateTaskWithTime(task)
        }
    suspend fun filterTasksByCategory(category: Int) : List<Task>?{
        return TaskManager.getTasks(category)
    }

    suspend fun filterTasksByWeekday(weekday: Int) : List<Task>?{
        return TaskManager.getTaskByDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    }

}