package com.example.HealthyMode.UI.Home_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.HealthyMode.UI.Auth.MainAuthentication
import com.example.HealthyMode.UI.Home.Home_screen
import com.example.HealthyMode.UI.profile.Profile
import com.example.HealthyMode.UI.step.StepsTrack
import com.example.HealthyMode.UI.water.Water
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.FragmentProfileFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.util.*

class profile_fragment : Fragment() {
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding:FragmentProfileFragmentBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileFragmentBinding.inflate(inflater,container,false)
        binding.edweight.text = Constant.loadData(requireActivity(), "weight", "curr_w", "0").toString()
        getlocation ()
        updateUI()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            startActivity(Intent(requireActivity(), Home_screen::class.java))
            requireActivity().finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        UserDetails()
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    fun getlocation ()
    {
        val locationManagerr = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(locationManagerr.isLocationEnabled)
        {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    currentLocation: Location?->
                if (currentLocation!=null && Constant.isInternetOn(requireActivity()))
                {
                    val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
                    val address = addresses!![0]
                    val land=address.subLocality
                    val state = address.adminArea
                    val country = address.countryName
//                    val district = address.subAdminArea
                    val locationName = address.locality
                    val FullAddress = "$land,$locationName,$state,$country"
                    binding.locat.text=FullAddress
                }
            }.addOnFailureListener {
                    exception->
                Toast.makeText(requireContext(), "Please Enable Your Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "Please Enable Your Location", Toast.LENGTH_SHORT).show()
        }
    }
    fun updateUI()
    {
        userDitails.addSnapshotListener { it,e->
            if(e!=null)return@addSnapshotListener
            if(it!=null)
            {
                binding.username.text = it.data?.get("fullname").toString()
                binding.email.text = it.data?.get("email").toString()
                binding.let.text = binding.username.text.toString()[0].toString()
            }
        }
        binding.apply {
            steps.setOnClickListener {
                startActivity(Intent(requireActivity(), StepsTrack::class.java))
            }
            edit.setOnClickListener {
                startActivity(Intent(activity, Profile::class.java))
            }
            waterT.setOnClickListener {
                startActivity(Intent(activity, Water::class.java))
            }
            logout.setOnClickListener {
                FirebaseAuth.getInstance().signOut();
                startActivity(Intent(activity, MainAuthentication::class.java))
                requireActivity().finish()
            }
            foodT.setOnClickListener {
//            startActivity(Intent(activity, Food_track::class.java))
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun UserDetails()
    {  userDitails.addSnapshotListener{it, error ->
        if(error!=null)return@addSnapshotListener
        if(it!=null && it.exists())
        {
            val dob=it.data?.get("dob").toString()
            val birthyear=dob.substring(5,9).toInt()
            var currentYear = LocalDate.now().year
            binding.age.text = (currentYear-birthyear).toString()
            binding.Gender.text=it.data?.get("gender").toString()
        }
    }
        val curruser=FirebaseAuth.getInstance().currentUser!!.uid
        val reference=Firebase.firestore.collection("user").document(curruser.toString())
        reference.collection("fitness").document("fit").addSnapshotListener{ it,e->
            if(e!=null)return@addSnapshotListener
            if (it!=null)
            {
                val heightf=it.data?.get("height").toString()
                val bmif=it.data?.get("Bmi").toString()
                binding.edhight.setText(if(heightf!="")heightf.toString() else "0")
                binding.bmi.text=if(bmif!="")bmif.toString() else "0"
                var bm:Double=0.0
                if(bmif.isEmpty() || bmif=="")
                {
                    bm=0.0
                }else {
                    bm = bmif?.toDouble()!!
                }
                if(bm<18.5)
                {
                    binding.measure.text="You are underweight"
                    binding.measure.setBackgroundColor(Color.RED)
                }
                else if(bm<29.9 && bm>25.0)
                {
                    binding.measure.text="You are Overweight"
                    binding.measure.setBackgroundColor(Color.RED)
                }
                else if(bm>30.0)
                {
                    binding.measure.text="You are Obese Range"
                    binding.measure.setBackgroundColor(Color.YELLOW)
                }else{
                    binding.measure.text="You are Normal and Healthy"
                    binding.measure.setBackgroundColor(Color.GREEN)
                }
            }
        }
    }
}