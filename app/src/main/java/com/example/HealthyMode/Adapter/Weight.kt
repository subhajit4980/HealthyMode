package com.example.HealthyMode.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.data_Model.weight
import com.example.HealthyMode.databinding.WeightListBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class Weight(options: FirestoreRecyclerOptions<weight>): FirestoreRecyclerAdapter<weight, Weight.ViewHolder>(options) {
    class ViewHolder(val binding: WeightListBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= WeightListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: weight) {
        holder.binding.date.text=model.date
        holder.binding.weight.text= "${model.weight} Kg"
    }
}