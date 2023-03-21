package com.example.HealthyMode.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.R
import com.example.HealthyMode.data_Model.Nutrient

class FoodNameAdapter(val foodlist:ArrayList<Nutrient>):
    RecyclerView.Adapter<FoodNameAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val food_name:TextView=itemView.findViewById(R.id.item_name)
        val cal:TextView=itemView.findViewById(R.id.calo)
        fun inialize(item: Nutrient)
        {
            food_name.text=item.foodName.toString()
            cal.text=item.calories.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.food_name,parent,false)
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return foodlist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.food_name.text=foodlist[position].foodName
        holder.inialize(foodlist[position])
    }
    private val touchHelper=object :ItemTouchHelper.SimpleCallback(
        0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position=viewHolder.adapterPosition
            foodlist.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun attachswipetoDelete(rec_v:RecyclerView)
    {
        val touchHelper=ItemTouchHelper(touchHelper)
        touchHelper.attachToRecyclerView(rec_v)
    }
}