package com.codinginflow.mvvmtodo.ui.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.TodoDatabase.Todo
import com.example.HealthyMode.databinding.TodoItemBinding

class Adapter_todo(private val listener: OnItemClickListener) :
    ListAdapter<Todo, Adapter_todo.TasksViewHolder>(DiffUtill()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }
    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
    inner class TasksViewHolder(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                status.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, status.isChecked)
                        check(status,desc)
                    }
                }
            }
        }

        fun bind(task: Todo) {
            binding.apply {
                status.isChecked=task.status
                desc.text = task.Desc
                sd.text=task.Start_date
                st.text=task.Start_time
                ed.text=task.End_date
                et.text=task.End_time
                check(status,desc)
            }
        }
    }
private fun check(status:CheckBox,desc:TextView)
{
    if (status.isChecked) {
        desc.paintFlags =
            desc.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        desc.paintFlags =
            desc.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}
    interface OnItemClickListener {
        fun onCheckBoxClick(task: Todo, isChecked: Boolean)
    }

    class DiffUtill : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem == newItem
    }

}