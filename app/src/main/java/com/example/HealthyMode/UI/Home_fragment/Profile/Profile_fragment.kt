package com.example.HealthyMode.UI.Home_fragment.Profile

import android.annotation.SuppressLint
import android.app.Dialog
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
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.HealthyMode.R
import com.example.HealthyMode.UI.Auth.MainAuthentication
import com.example.HealthyMode.UI.Home.Home_screen
import com.example.HealthyMode.UI.step.StepsTrack
import com.example.HealthyMode.UI.water.Water
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.Utils.UIstate
import com.example.HealthyMode.databinding.FragmentProfileFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.floor
import kotlin.math.round
import kotlin.properties.Delegates

class profile_fragment : Fragment() {
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding:FragmentProfileFragmentBinding
    private lateinit var dialog: Dialog
    private  lateinit var viewModel:Profile_ViewModel
    private val df = DecimalFormat("#.##")
    private var height by Delegates.notNull<Double>()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileFragmentBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[Profile_ViewModel::class.java]
        binding.edweight.text = Constant.loadData(requireActivity(), "weight", "curr_w", "0").toString()
        getlocation ()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog= Dialog(requireContext())
        UserDetails()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            startActivity(Intent(requireActivity(), Home_screen::class.java))
            requireActivity().finish()
        }
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
        dialog.setContentView(R.layout.pop_height)
        val ft: NumberPicker = dialog.findViewById(R.id.ft)
        val inch: NumberPicker = dialog.findViewById(R.id.inch)
        val add: AppCompatButton = dialog.findViewById(R.id.add)
        ft.minValue = 3
        ft.maxValue = 8
        ft.value= floor(height).toInt()
        ft.wrapSelectorWheel = true
        inch.minValue = 1
        inch.maxValue = 12
        inch.value= round((height-floor(height))*12).toInt()
        inch.wrapSelectorWheel = true
        binding.apply {
            steps.setOnClickListener {
                startActivity(Intent(requireActivity(), StepsTrack::class.java))
            }
            edit.setOnClickListener {
//                startActivity(Intent(activity, Profile::class.java))
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
            height.setOnClickListener {
                    dialog.show()
                }
                add.setOnClickListener {
                    var height=ft.value.toDouble()+(inch.value.toDouble()/12)
                    height=df.format(height).toDouble()
                    viewModel.updateHeight(height.toString(),requireContext())
                    dialog.dismiss()
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun UserDetails()
    {
        userDitails.addSnapshotListener{it, error ->
        if(error!=null)return@addSnapshotListener
        if(it!=null && it.exists())
        {
            binding.username.text = it.data?.get("fullname").toString()
            binding.email.text = it.data?.get("email").toString()
            binding.let.text = binding.username.text.toString()[0].toString()
            val dob=it.data?.get("dob").toString()
            val birthyear=dob.substring(dob.length-4).toInt()
            var currentYear = LocalDate.now().year
            binding.age.text = (currentYear-birthyear).toString()
            binding.Gender.text=it.data?.get("gender").toString()
        }
         }
        viewModel.getHeight()
        viewModel.data.observe(requireActivity()){ state->
            when (state){
                is UIstate.Loading -> {
                    binding.content.visibility=View.GONE
                    binding.progressBar.visibility=View.VISIBLE
                }
                is UIstate.Failure -> {
                    Toast.makeText(requireContext(), state.error.toString(), Toast.LENGTH_SHORT).show()
                }
                is UIstate.Success -> {
                    binding.content.visibility=View.VISIBLE
                    binding.progressBar.visibility=View.GONE
                    binding.edhight.text=state.data.toString()
                }
            }
        }
        val curruser=FirebaseAuth.getInstance().currentUser!!.uid
        val reference=Firebase.firestore.collection("user").document(curruser.toString())
        reference.addSnapshotListener{ it,e->
            if(e!=null)return@addSnapshotListener
            if (it!=null)
            {
                var heightf= it.data!!["height"]?.toString()?.toDouble()?: 0.0
                height=heightf
                heightf *= 0.305
                var Bmi:String="0"
                val weight=Constant.loadData(requireActivity(), "weight", "curr_w", "0").toString().toDouble()
                if(weight ==0.0 || heightf==0.0)
                {
                    Bmi="0"
                }else {
                    Bmi = Math.round(((weight / (heightf * heightf))).toDouble())
                        .toString()
                }
                binding.bmi.text=Bmi
                var bm=Bmi.toDouble()
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
                updateUI()
            }
        }
    }
}