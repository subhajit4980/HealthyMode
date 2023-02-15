
package com.example.spodemy.Home.water

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.Adapter.WAdapter
import com.example.spodemy.data.water
import com.example.spodemy.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_water.*

class Water : AppCompatActivity() {
    private lateinit var w_RecyclerView: RecyclerView
    lateinit var adapter: WAdapter
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        water_data()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun water_data() {
        val ref=userDitails.collection("water track").orderBy("Date",Query.Direction.DESCENDING)
        val query=ref
        val recyclerOptions= FirestoreRecyclerOptions.Builder<water>().setQuery(query,water::class.java).build()
        adapter= WAdapter(recyclerOptions)
        w_RecyclerView=findViewById(R.id.rc)
        w_RecyclerView.layoutManager=LinearLayoutManager(this)
        w_RecyclerView.adapter=adapter
        adapter.startListening()
    }
}