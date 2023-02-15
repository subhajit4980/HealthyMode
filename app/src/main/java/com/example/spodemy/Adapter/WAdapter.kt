package com.example.spodemy.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.Adapter.WAdapter.*
import com.example.spodemy.Home.water.Water
import com.example.spodemy.R
import com.example.spodemy.data.water
import com.example.spodemy.databinding.RecWaterBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class WAdapter(options:FirestoreRecyclerOptions<water>):FirestoreRecyclerAdapter<water, ViewHolder>(options) {
    class ViewHolder(val binding: RecWaterBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=RecWaterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: water) {
        holder.binding.date.text=model.Date
        holder.binding.noGlass.text= model.glass.toString()
    }
}