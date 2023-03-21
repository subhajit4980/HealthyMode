package com.example.HealthyMode.All_View.Home_fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.HealthyMode.All_View.Authentication_View.MainAuthentication
import com.example.HealthyMode.All_View.Home.Home_screen
import com.example.HealthyMode.All_View.profile.Profile
import com.example.HealthyMode.All_View.step.StepsTrack
import com.example.HealthyMode.All_View.water.Water
import com.example.HealthyMode.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
            startActivity(Intent(requireActivity(), StepsTrack::class.java))
        }
        val edit: ImageView = root!!.findViewById(R.id.edit)
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
            startActivity(Intent(activity, Water::class.java))
        }
        val food: LinearLayout = root!!.findViewById(R.id.foodT)
        food.setOnClickListener {
//            startActivity(Intent(activity, Food_track::class.java))
        }
        val logout: CardView = root!!.findViewById(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(activity, MainAuthentication::class.java))
            requireActivity().finish()
        }
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            startActivity(Intent(requireActivity(), Home_screen::class.java))
//            requireActivity().overridePendingTransition(R.anim.left_center, R.anim.right_center);
            requireActivity().finish()
        }
    }
}