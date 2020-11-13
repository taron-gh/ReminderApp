package com.acaproject.reminderapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class TaskRecyclerAdapter(private val tasks: List<Task> = mutableListOf(), private var clickListener: OnTaskClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //    private var tasks: List<Task> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> holder.bind(tasks[position],clickListener)

        }
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int = tasks.size

    //   fun submitList(taskList: List<Task>){
//       tasks = taskList
//   }
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val taskTextView: TextView = itemView.taskTextView
        private val dateTimeTextView: TextView = itemView.dateTimeTextView
        @SuppressLint("SetTextI18n")
        fun bind(task: Task, listener: OnTaskClickListener) {
            val calendar = Calendar.getInstance()
            val context = itemView.context
            val daysOfWeek = listOf(context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wednesday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday))
            calendar.timeInMillis = task.currentTime
            taskTextView.text = task.name
            dateTimeTextView.text = "${daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]}" +
                    " ${calendar.get(Calendar.HOUR_OF_DAY)} " +
                    "${calendar.get(Calendar.MINUTE)} " +
                    amPm(calendar.get(java.util.Calendar.AM_PM))
            itemView.setOnClickListener {
                listener.onItemClick(task)
            }
        }
    }
}

private fun amPm(hour:Int):String{
    if (hour == 0) {return "AM"}

    if (hour == 1) {return "PM"}

    return "NV"

}

interface OnTaskClickListener {
    fun onItemClick(task: Task)
}