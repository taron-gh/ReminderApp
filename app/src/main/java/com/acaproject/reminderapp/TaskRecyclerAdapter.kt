package com.acaproject.reminderapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.ArrayList

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
//            taskTextView.text = task.name
//            dateTimeTextView.text = "${task.dayOfWeek} ${task.hour} ${task.minute}"

            itemView.setOnClickListener {
                listener.onItemClick(task)
            }
        }
    }
}

interface OnTaskClickListener {
    fun onItemClick(task: Task)
}