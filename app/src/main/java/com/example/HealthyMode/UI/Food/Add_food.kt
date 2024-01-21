package com.example.HealthyMode.UI.Food

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.Adapter.FoodAdapter
import com.example.HealthyMode.data_Model.Nutrient
import com.example.HealthyMode.databinding.ActivityAddFoodBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class Add_food : AppCompatActivity() {
    private lateinit var binding: ActivityAddFoodBinding
    private var userDitails: DocumentReference = Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid.toString()
    )
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val intent = Intent(this, SearchMeal::class.java)
        binding.bf.setOnClickListener {
            intent.putExtra("key1", "breakfast")
            startActivity(intent)
            finish()
        }
        binding.ms.setOnClickListener {
            intent.putExtra("key1", "morn_snack")
            startActivity(intent)
            finish()
        }
        binding.lunch.setOnClickListener {
            intent.putExtra("key1", "lunch")
            startActivity(intent)
            finish()
        }
        binding.evSn.setOnClickListener {
            intent.putExtra("key1", "evening")
            startActivity(intent)
            finish()
        }
        binding.din.setOnClickListener {
            intent.putExtra("key1", "dinner")
            startActivity(intent)
            finish()
        }
//        Handler().post(
//           object :Runnable{
//               @RequiresApi(Build.VERSION_CODES.O)
//               override fun run() {
        rec_view("breakfast", binding.bT, binding.breakfastRv1)
        rec_view("morn_snack", binding.Mt, binding.msRv)
        rec_view("lunch", binding.lun, binding.lunchRv)
        rec_view("evening", binding.evSnack, binding.evRv)
        rec_view("dinner", binding.dinT, binding.dinRv)
//                   Handler().postDelayed(this, 400)
//               }
//           })

    }

    //    fun rec_view(message:TextView,rc:RecyclerView,list:ArrayList<Nutrient>) {
//        if(list.size>=1)
//        {
//            rc.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            message.visibility=View.GONE
//            rc.visibility=View.VISIBLE
//            rc.layoutManager= LinearLayoutManager(this)
//            rc.adapter=FoodNameAdapter(list)
//            FoodNameAdapter(list).attachswipetoDelete(rc)
//        }else{
//            message.visibility=View.VISIBLE
//            rc.visibility=View.GONE
//        }
//    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun rec_view(time: String, message: TextView, rc: RecyclerView) {
        val foodlist= arrayListOf<Nutrient>()
        val query =
            userDitails.collection("Meals").document(LocalDate.now().toString()).collection(time)
        query.addSnapshotListener { value, error ->
            if (value != null && value.size() != 0) {
                message.visibility = View.GONE
                rc.visibility = View.VISIBLE
            } else {
                message.visibility = View.VISIBLE
                rc.visibility = View.GONE
            }
        }
        val recyclerOptions =
            FirestoreRecyclerOptions.Builder<Nutrient>().setQuery(query, Nutrient::class.java)
                .build()
        val adapter = FoodAdapter(this,time, recyclerOptions)
        rc.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        rc.layoutManager = LinearLayoutManager(this)
        rc.adapter = adapter
        adapter.startListening()
        adapter.attachswipetoDelete(rc)
    }
}