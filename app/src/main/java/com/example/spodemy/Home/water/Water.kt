
package com.example.spodemy.Home.water

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.data.water
import com.example.spodemy.R
import com.example.spodemy.Adapter.Wateradapter
import com.example.spodemy.data.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_water.*
import java.time.LocalDate
class Water : AppCompatActivity() {
    private lateinit var w_RecyclerView: RecyclerView
    lateinit var adapter: Wateradapter
    private lateinit var rec_Arraylist: ArrayList<water>
    private var no_glass:Int?=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)
        val curr_date=LocalDate.now()
        val Ref = FirebaseDatabase.getInstance()
            .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/Water/${curr_date.toString()}")
        Ref.get().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val glass: String? = dataSnapshot.child("no_glass").value?.toString()
                try {
                    if(glass == ""||glass==null || glass.isEmpty())
                    {
                        no_glass=0
                    }else no_glass=glass.toInt()
                }catch (_:Exception)
                {
                    no_glass=0
                }


            } else {
                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        w_RecyclerView=findViewById(R.id.rc)
        w_RecyclerView.layoutManager=LinearLayoutManager(this)
        rec_Arraylist= arrayListOf<water>()
        water_data()
        add_water.setOnClickListener{
            no_glass = no_glass?.plus(1)
            val ref: DatabaseReference =
                FirebaseDatabase.getInstance()
                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
            val waterr = water(curr_date.toString(),no_glass)
            ref.child("Water")
                .child(curr_date.toString())
                .setValue(waterr)
                .addOnSuccessListener {
                    Toast.makeText(this, "You take one glass now", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun water_data() {
        val dbref:DatabaseReference = FirebaseDatabase.getInstance().getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/Water")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rec_Arraylist.clear()
                try {
                    if (snapshot.exists()) {
                        for (bookSnapshot in snapshot.children) {
                            val book = bookSnapshot.getValue(water::class.java)!!
                            rec_Arraylist.add(book)
                        }
                        try{
                            if (rec_Arraylist.size > 1) {
                                rec_Arraylist = rec_Arraylist.reversed() as ArrayList<water>
                            }
                        }catch (_:Exception){

                        }
                        adapter = Wateradapter(rec_Arraylist, this@Water)
                        w_RecyclerView.adapter = adapter
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@Water, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}