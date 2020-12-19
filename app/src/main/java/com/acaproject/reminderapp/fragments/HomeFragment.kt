package com.acaproject.reminderapp.fragments

import android.R.attr.fragment
import android.R.attr.key
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.acaproject.reminderapp.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import java.util.*
import java.lang.IllegalStateException
import java.text.FieldPosition


class HomeFragment : Fragment(), OnTaskClickListener {

    private lateinit var fragmentControl: FragmentControl
    private lateinit var addedTask: Task
    private val tasks = mutableListOf<Task>()


    private val todaySpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (parent?.getItemAtPosition(position).toString()) {
                "All Tasks" ->{
                    GlobalScope.launch {
                        val list= TaskManager.getAllTasks()
                        withContext(Dispatchers.Main){
                            taskAdapter.updateList(list)
                        }
                    }
                }
                "Today" -> {
                    GlobalScope.launch {
                       val list= TaskManager.getTodayTasks()
                       withContext(Dispatchers.Main){
                           taskAdapter.updateList(list)
                       }
                    }
                }
            }
        }

    }

    private val categorySpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            val filteredTasks = tasks.filter {
//                it.category == parent?.getItemAtPosition(position).toString()
//            }
            GlobalScope.launch {
                val list = TaskManager.getTasks(parent?.getItemAtPosition(position).toString())
                withContext(Dispatchers.Main) {
                    taskAdapter.updateList(list)
                }
            }
        }
    }


    private val weekSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            GlobalScope.launch {
                when (parent?.getItemAtPosition(position).toString()) {
                    getString(R.string.sunday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.SUNDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                    getString(R.string.monday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.MONDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                    getString(R.string.tuesday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.TUESDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                    getString(R.string.wednesday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.WEDNESDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                    getString(R.string.thursday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.THURSDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                    getString(R.string.friday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.FRIDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                    getString(R.string.saturday) -> {
                        val list = TaskManager.getTaskByDayOfWeek(Calendar.SATURDAY)
                        withContext(Dispatchers.Main) {
                            taskAdapter.updateList(list)
                        }
                    }
                }
            }
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

    private fun initSpinners() {

        val adapterToday = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Today,
                android.R.layout.simple_spinner_item
            )
        }

        adapterToday?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_today.adapter = adapterToday
        spinner_today.onItemSelectedListener = todaySpinnerListener


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

    override fun onItemLongClick(task: Task, position: Int) {
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

    override fun editTaskPage(task: Task, position: Int) {
        val editFragment = EditFragment(task)
        val bundle = Bundle()
        bundle.putInt("position", position)
        editFragment.arguments = bundle
        fragmentControl.openPage("Edit Task", true, editFragment)
    }


    private fun addDataSet() {

        taskAdapter = TaskRecyclerAdapter(tasks, this)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.todayMenu -> {

                    spinner_category.visibility = View.GONE
                    spinner_week.visibility = View.GONE
                    spinner_today.visibility = View.VISIBLE

                    return@OnNavigationItemSelectedListener true
                }

                R.id.categoryMenu -> {

                    spinner_today.visibility = View.GONE
                    spinner_week.visibility = View.GONE
                    spinner_category.visibility = View.VISIBLE

                    return@OnNavigationItemSelectedListener true
                }
                R.id.weekMenu -> {

                    spinner_today.visibility = View.GONE
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

    fun edit(task: Task, position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            TaskManager.updateTaskWithTime(task)
        }
        taskAdapter.updateListItem(position, task)
        suspend fun filterTasksByCategory(category: Int): List<Task>? {
            return TaskManager.getTasks(getString(category))
        }

        suspend fun filterTasksByWeekday(weekday: Int): List<Task>? {
            return TaskManager.getTaskByDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
        }

    }
}