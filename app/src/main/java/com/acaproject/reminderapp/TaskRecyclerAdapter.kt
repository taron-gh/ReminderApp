package com.acaproject.reminderapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class TaskRecyclerAdapter(
    private var tasks: MutableList<Task> = mutableListOf(),
    private var clickListener: OnTaskClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                holder.bind(tasks[position], clickListener, position)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val taskTextView: TextView = itemView.taskTextView
        private val dateTimeTextView: TextView = itemView.dateTimeTextView
        private val editBtn:ImageButton=itemView.editBtn

        @SuppressLint("SetTextI18n")
        fun bind(task: Task, listener: OnTaskClickListener, position: Int) {
            val calendar = Calendar.getInstance()
            val context = itemView.context
            val daysOfWeek = listOf(
                context.getString(R.string.sunday),
                context.getString(R.string.monday),
                context.getString(R.string.tuesday),
                context.getString(R.string.wednesday),
                context.getString(R.string.thursday),
                context.getString(R.string.friday),
                context.getString(R.string.saturday)
            )
            calendar.timeInMillis = task.currentTime
            taskTextView.text = task.name
            dateTimeTextView.text = "${daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]}" +
                    " ${calendar.get(Calendar.HOUR_OF_DAY)} " +
                    "${calendar.get(Calendar.MINUTE)} " +
                    amPm(calendar.get(java.util.Calendar.AM_PM))

            itemView.setOnClickListener {
                listener.onItemClick(task)
            }

            itemView.setOnLongClickListener {
                listener.onItemLongClick(task, position)
                true
            }

            editBtn.setOnClickListener {
                listener.editTaskPage(task, position)
            }


        }


    }

    fun updateList (list: List<Task>) {
        tasks=list as MutableList<Task>
        notifyDataSetChanged()
    }
    fun updateListItem (position: Int, task: Task) {
        tasks[position] = task
        notifyDataSetChanged()
    }

}


private fun amPm(hour: Int): String {
    if (hour == 0) {
        return "AM"
    }

    if (hour == 1) {
        return "PM"
    }

    return "NV"

}

interface OnTaskClickListener {
    fun onItemClick(task: Task)
    fun onItemLongClick(task: Task, position: Int)
    fun editTaskPage(task:Task, position: Int)

}