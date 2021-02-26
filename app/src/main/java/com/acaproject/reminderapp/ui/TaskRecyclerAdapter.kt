package com.acaproject.reminderapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acaproject.reminderapp.R
import com.acaproject.reminderapp.Task
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
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
        private val editBtn: ImageView = itemView.editBtn

        @SuppressLint("SetTextI18n")
        fun bind(task: Task, listener: OnTaskClickListener, position: Int) {
            val calendar = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("hh:mm")

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
                    " ${timeFormat.format(calendar.time)} " +

                    amPm(calendar.get(Calendar.AM_PM))

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

    fun updateList (list: List<Task>?) {
        if(list != null){
            tasks=list as MutableList<Task>
            notifyDataSetChanged()
        }
    }
    fun updateListItem (position: Int, task: Task) {
        tasks[position] = task
        notifyDataSetChanged()
    }
    fun removeListItem (position: Int) {
        tasks.removeAt(position) // may produce IndexOutOfBoundsException
        notifyItemRemoved(position)
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
    fun editTaskPage(task: Task, position: Int)

}