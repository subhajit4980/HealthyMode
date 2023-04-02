package com.example.HealthyMode.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.data_Model.Nutrient
import com.example.HealthyMode.databinding.FoodListBinding

class FoodNameAdapter(val foodlist:ArrayList<Nutrient>):
    RecyclerView.Adapter<FoodNameAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: FoodListBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FoodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return foodlist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(foodlist[position]){
                binding.itemName.text = this.foodName
                binding.calo.text = this.calories
                binding.amount.text = this.quantity
                binding.unit.text = this.unit
            }
        }
    }

//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        with(holder){
//            with(foodlist[position]){
//                binding.text = this.name
//                binding.tvExp.text = this.exp
//            }
//        }
//        holder.apply {
//            binding.food_name.text=foodlist[position].foodName
//            cal.text=foodlist[position].calories
//
//        }
//
//    }
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
            try {
                val position=viewHolder.adapterPosition
                foodlist.removeAt(position)
                notifyItemRemoved(position)
            }catch(_:IndexOutOfBoundsException){}

        }
    }
    fun attachswipetoDelete(rec_v:RecyclerView)
    {
        val touchHelper=ItemTouchHelper(touchHelper)
        touchHelper.attachToRecyclerView(rec_v)
    }
}