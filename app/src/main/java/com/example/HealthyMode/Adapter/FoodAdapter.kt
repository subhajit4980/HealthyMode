package com.example.HealthyMode.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.data_Model.Nutrient
import com.example.HealthyMode.databinding.FoodListBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
class FoodAdapter(val context:Context,val time:String,options: FirestoreRecyclerOptions<Nutrient>): FirestoreRecyclerAdapter<Nutrient, FoodAdapter.ViewHolder>(options) {
    private var userDitails: DocumentReference = Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid
    )
    class ViewHolder(val binding: FoodListBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= FoodListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Nutrient) {
        holder.binding.itemName.text=model.foodName
        holder.binding.amount.text=model.quantity
        holder.binding.unit.text=model.unit
        holder.binding.calo.text=model.calories
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

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val item = getItem(position)
            // Delete item from Firestore
            userDitails.collection("Meals").document(LocalDate.now().toString()).collection(time)
                .document(item.foodName!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "$time ${item.foodName} deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Show error message here
                }
        }
    }
    fun attachswipetoDelete(rec_v:RecyclerView)
    {
        val itemtouchHelper=ItemTouchHelper(touchHelper)
        itemtouchHelper.attachToRecyclerView(rec_v)
    }

}