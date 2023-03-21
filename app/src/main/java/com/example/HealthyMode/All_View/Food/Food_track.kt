package com.example.HealthyMode.All_View.Food

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.Adapter.FoodAdapter
import com.example.HealthyMode.data_Model.Food

class Food_track : AppCompatActivity() {
    private lateinit var w_RecyclerView: RecyclerView
    lateinit var adapter: FoodAdapter
    private lateinit var rec_Arraylist: ArrayList<Food>
    private var food_it:String?=null
    private var dialog:Dialog?=null
//    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activ)
//        val curr_date= LocalDate.now()
//        w_RecyclerView=findViewById(R.id.frc)
//        w_RecyclerView.layoutManager= LinearLayoutManager(this)
//        rec_Arraylist= arrayListOf<Food>()
//        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        food_data()
//        dialog= Dialog(this)
//        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"))
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//        val second = calendar.get(Calendar.SECOND)
//        val formattedTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}:${String.format("%02d", second)}"
//        add_food.setOnClickListener {
//            dialog!!.setContentView(R.layout.pop_weight)
//            dialog!!.show()
////            val input:TextInputEditText=dialog!!.findViewById(R.id.foodt)
//            val add:AppCompatButton=dialog!!.findViewById(R.id.add)
//            val ref: DatabaseReference =
//                FirebaseDatabase.getInstance()
//                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
//                val Ref = FirebaseDatabase.getInstance()
//                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/foods/${curr_date.toString()}")
//                    Ref.get().addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val dataSnapshot = task.result
//                            food_it= dataSnapshot.child("foods").value?.toString()
//
//                        } else {
//                            Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
////            add.setOnClickListener {
////                try{
////                    if (input.text!!.isNotEmpty()) {
////                        if (food_it != null) {
////                            val fod = Food(
////                                curr_date.toString(),
////                                input.text.toString() + " "+formattedTime.toString() + "\n" + food_it.toString()
////                            )
////                            ref.child("foods")
////                                .child(curr_date.toString())
////                                .setValue(fod)
////                                .addOnSuccessListener {
////                                }
////                        } else {
////                            val fod = Food(curr_date.toString(), input.text.toString()+ " "+ formattedTime.toString())
////                            ref.child("foods")
////                                .child(curr_date.toString())
////                                .setValue(fod)
////                                .addOnSuccessListener {
////                                }
////                        }
////                        dialog!!.dismiss()
////                    } else {
////                        input.error = "put food item"
////                        return@setOnClickListener
////                    }
////                }catch (_:Exception)
////                {
////
////                }
////            }
//        }
//    }
//
//    private fun food_data() {
//        val dbref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/foods")
//        dbref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                rec_Arraylist.clear()
//                try {
//                    if (snapshot.exists()) {
//                        for (bookSnapshot in snapshot.children) {
//                            val dns = bookSnapshot.getValue(Food::class.java)!!
//                            rec_Arraylist.add(dns)
//                        }
//                        try{
//                            if (rec_Arraylist.size > 1) {
//                                rec_Arraylist = rec_Arraylist.reversed() as ArrayList<Food>
//                            }
//                        }catch (_:Exception){
//
//                        }
//                        adapter = FoodAdapter(rec_Arraylist,this@Food_track)
//                        w_RecyclerView.adapter = adapter
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(this@Food_track, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//    }
}