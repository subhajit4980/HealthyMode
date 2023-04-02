//package com.example.HealthyMode.Adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.example.HealthyMode.TodoDatabase.Todo
//import com.example.HealthyMode.databinding.TodoItemBinding
//
//class Adapter_todo: PagingDataAdapter<Todo, Adapter_todo.MainViewHolder>(DIFF_CALLBACK) {
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>() {
//            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean =
//                oldItem.id == newItem.id
//
//            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
//                oldItem == newItem
//        }
//    }
//    inner class MainViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
//        return MainViewHolder(
//            TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//    }
//    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        val item = getItem(position)
//
//        holder.binding.apply {
//            desc.text=item!!.Desc.toString()
//            sd.text=item.Start_date.toString()
//            st.text=item.Start_time.toString()
//            ed.text=item.End_date.toString()
//            et.text=item.End_time.toString()
//        }
//    }
//}