package com.example.spodemy.Home.fragment

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
import com.example.spodemy.Home.Food.Food_track
import com.example.spodemy.Home.profile.Profile
import com.example.spodemy.Home.water.Water
import com.example.spodemy.data.water
import com.example.spodemy.Authentication_Asset.MainAuthentication
import com.example.spodemy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile_fragment.*
import java.time.LocalDate
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class profile_fragment : Fragment() {
    private  var root:View?=null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
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
        val edit: ImageView = root!!.findViewById(R.id.edit)
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance()
                .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
        activity?.let {
            ref.get().addOnCompleteListener(it) { task ->
                if (task.isSuccessful) {
                    val ds = task.result
                    val name: TextView = root!!.findViewById(R.id.username)
                    name.text = ds.child("name").value.toString()
                    val email: TextView = root!!.findViewById(R.id.email)
                    email.text = ds.child("email").value.toString()
                    val let: TextView = root!!.findViewById(R.id.let)
                    let.text = name.text.toString()[0].toString()
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            activity?.let { it1 ->
                ActivityCompat.requestPermissions(
                    it1,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
        edit.setOnClickListener {
            startActivity(Intent(activity, Profile::class.java))
        }
        val water: CardView = root!!.findViewById(R.id.watercd)
        water.setOnClickListener {
            val curr_date = LocalDate.now()
            val waterr = water(curr_date.toString(), 0)
            val ref: DatabaseReference =
                FirebaseDatabase.getInstance()
                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/Water")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChild(curr_date.toString())) {
                        ref.child(curr_date.toString())
                            .setValue(waterr)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            startActivity(Intent(activity, Water::class.java))
        }
        val food: CardView = root!!.findViewById(R.id.food)
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
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can access the location here
            } else {
                // Permission denied, you can show a message to the user
                Toast.makeText(activity, "Please give the location permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

}