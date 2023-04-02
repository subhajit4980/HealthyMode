package com.example.HealthyMode.UI.Home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.FragmentAddWeightBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class AddWeight : Fragment() {
    private lateinit var binding:FragmentAddWeightBinding
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid.toString())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding= FragmentAddWeightBinding.inflate(inflater,container,false)
        binding.weight.minValue=1
        binding.weight.maxValue=200
        binding.add.setOnClickListener {
            addweight()
            startActivity(Intent(requireActivity(), Home_screen::class.java))
            requireActivity().finish()
        }
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addweight()
    {
        val weight=binding.weight.value.toString()
        val curr_date=LocalDate.now().toString()
        Constant.savedata(requireContext(), "weight", "curr_w", weight.toString())
        val map= hashMapOf("weight" to weight,  "date" to curr_date)
        userDitails.collection("Weight track").document(curr_date).set(map)
    }
}