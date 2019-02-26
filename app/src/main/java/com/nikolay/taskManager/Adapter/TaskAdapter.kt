package com.nikolay.taskManager.Adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.nikolay.taskManager.Models.Task
import com.nikolay.taskManager.R
import com.nikolay.taskManager.SQLite.FeedReaderDbHelper
import kotlinx.android.synthetic.main.item_task.view.*


class TaskAdapter(private val taskList: List<Task>, private val listener: (Task) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position], listener)
        setAnimation(holder.itemView)


    }

    private fun setAnimation(viewToAnimate: View) {
        if (viewToAnimate.animation == null) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.animation = animation
        }
    }

    override fun getItemCount() = taskList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(rootView)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")

        fun bind(task: Task, listener: (Task) -> Unit) = with(itemView) {
            tvTitle.text = task.title
            tvTime.text = task.date + task.time
            tvPriority.text = task.Priority
            checkbox.isChecked = task.done
            val dbHelper = FeedReaderDbHelper(context)
            setOnClickListener { listener(task) }
            checkbox.setOnClickListener {

                val editedTask =
                    Task(
                        task.id,
                        task.title,
                        checkbox.isChecked,
                        task.Priority,
                        task.PriorityGrade,
                        task.date,
                        task.time
                    )
                dbHelper.updateTask(editedTask)
                Log.d("adapterDone", editedTask.toString() + checkbox.isChecked)
            }

        }

    }

}


