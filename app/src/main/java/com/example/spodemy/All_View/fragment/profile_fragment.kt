package com.example.spodemy.All_View.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.spodemy.All_View.Food.Food_track
import com.example.spodemy.All_View.profile.Profile
import com.example.spodemy.All_View.water.Water
import com.example.spodemy.Authentication_Asset.MainAuthentication
import com.example.spodemy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile_fragment.*
import android.widget.LinearLayout
import com.example.spodemy.All_View.Steps_Track
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class profile_fragment : Fragment() {
    private  var root:View?=null
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile_fragment, container, false)
        val steps:LinearLayout=root!!.findViewById(R.id.steps)
        steps.setOnClickListener {
            startActivity(Intent(requireActivity(),Steps_Track::class.java))
        }



        val edit: ImageView = root!!.findViewById(R.id.edit)
//        val ref: DatabaseReference =
//            FirebaseDatabase.getInstance()
//                .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
//        activity?.let {
//            ref.get().addOnCompleteListener(it) { task ->
//                if (task.isSuccessful) {
//                    val ds = task.result
//                    val name: TextView = root!!.findViewById(R.id.username)
//                    name.text = ds.child("name").value.toString()
//                    val email: TextView = root!!.findViewById(R.id.email)
//                    email.text = ds.child("email").value.toString()
//                    val let: TextView = root!!.findViewById(R.id.let)
//                    let.text = name.text.toString()[0].toString()
//                }
//            }
//        }
        userDitails.get().addOnSuccessListener {
            if(it!=null)
            {
                val name: TextView = root!!.findViewById(R.id.username)
                name.text = it.data?.get("fullname").toString()
                val email: TextView = root!!.findViewById(R.id.email)
                email.text = it.data?.get("email").toString()
                val let: TextView = root!!.findViewById(R.id.let)
                let.text = name.text.toString()[0].toString()
            }
        }

        edit.setOnClickListener {
            startActivity(Intent(activity, Profile::class.java))
        }
        val water: LinearLayout = root!!.findViewById(R.id.waterT)
        water.setOnClickListener {
//            val curr_date = LocalDate.now()
//            val water= mapOf(
//                "Date" to curr_date.toString(),
//                "No of glass" to "0"
//            )
//            userDitails.collection("water track").document(curr_date.toString()).get().addOnSuccessListener {
//                snapshot->
//                if (snapshot.exists())
//                {
//
//                }else{
//                    userDitails.collection("water track").document(curr_date.toString()).set(water)
//                }
//            }

            startActivity(Intent(activity, Water::class.java))
        }
        val food: LinearLayout = root!!.findViewById(R.id.foodT)
        food.setOnClickListener {
            startActivity(Intent(activity, Food_track::class.java))
        }
        val logout: CardView = root!!.findViewById(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(activity, MainAuthentication::class.java))
            requireActivity().finish()
        }
        return root
    }


}


//            val waterr = water(curr_date.toString(), 0)
//            val ref: DatabaseReference =
//                FirebaseDatabase.getInstance()
//                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/Water")
//            ref.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (!snapshot.hasChild(curr_date.toString())) {
//                        ref.child(curr_date.toString())
//                            .setValue(waterr)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })