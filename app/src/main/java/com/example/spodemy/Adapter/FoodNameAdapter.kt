package com.example.spodemy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.R
import com.example.spodemy.data.foodName

class FoodNameAdapter(val foodlist:ArrayList<foodName>):
    RecyclerView.Adapter<FoodNameAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val food_name:TextView=itemView.findViewById(R.id.food_name)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.food_name,parent,false)
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return foodlist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.food_name.text=foodlist[position].name
    }
}