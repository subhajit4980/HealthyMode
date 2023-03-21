package com.example.HealthyMode.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.R
import com.example.HealthyMode.TodoDatabase.Todo

class TodoAdapter(private val fragment: Fragment):
    RecyclerView.Adapter<TodoAdapter.MyViewHolder>() {
    private var plans:List<Todo> = listOf()
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val status:CheckBox=itemView.findViewById(R.id.status)
        val desc:TextView=itemView.findViewById(R.id.desc)
        val sd:TextView=itemView.findViewById(R.id.sd)
        val st:TextView=itemView.findViewById(R.id.st)
        val ed:TextView=itemView.findViewById(R.id.ed)
        val et:TextView=itemView.findViewById(R.id.et)
        fun inialize(item: Todo)
        {
           desc.text=item.Desc.toString()
            sd.text=item.Start_date.toString()
            st.text=item.Start_time.toString()
            ed.text=item.End_date.toString()
            et.text=item.End_time.toString()
        }
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val pop=PopupMenu()
        holder.inialize(plans[position])

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.todo_item,parent,false)
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return plans.size
    }
    fun planslist(list:List<Todo>){
        plans=list
        notifyDataSetChanged()
    }
}