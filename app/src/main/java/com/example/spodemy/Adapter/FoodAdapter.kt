package com.example.spodemy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.data.Food
import com.example.spodemy.R

class FoodAdapter(private  val foodd:ArrayList<Food>, private val context: Context):RecyclerView.Adapter<FoodAdapter.MyVIewHolder> (){

        inner class MyVIewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val date: TextView =itemView.findViewById(R.id.fdate)
            private  val  foods: TextView =itemView.findViewById(R.id.food_item)
            @SuppressLint("UseCompatLoadingForDrawables")
            fun initialize(item: Food){
                date.text=item.date
                foods.text=item.foods.toString()
            }

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVIewHolder {
            val itemView=
                LayoutInflater.from(parent.context).inflate(R.layout.food_trck,parent,false)
            return  MyVIewHolder(itemView)
        }
        override fun getItemCount(): Int {
            return foodd.size
        }
    override fun onBindViewHolder(holder: MyVIewHolder, position: Int) {
        holder.initialize(foodd[position])
    }
}