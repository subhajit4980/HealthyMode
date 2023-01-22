package com.example.spodemy.Adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.widget.TextView
import com.example.spodemy.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.rec_water.view.*
import com.example.spodemy.data.water

class Wateradapter(private  val re_water:ArrayList<water>, private val context: Context):RecyclerView.Adapter<Wateradapter.MyVIewHolder> (){

    inner class MyVIewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val date:TextView=itemView.findViewById(R.id.date)
        private  val  no_g:TextView=itemView.findViewById(R.id.no_glass)
        @SuppressLint("UseCompatLoadingForDrawables")
        fun initialize(item: water){
            date.text=item.date
            no_g.text=item.no_glass.toString()
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVIewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.rec_water,parent,false)
        return  MyVIewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyVIewHolder, position: Int) {
        holder.initialize(re_water[position])
    }

    override fun getItemCount(): Int {
        return re_water.size
    }

}