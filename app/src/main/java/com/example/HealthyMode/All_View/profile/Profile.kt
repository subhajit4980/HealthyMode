package com.example.HealthyMode.All_View.profile

//import kotlinx.android.synthetic.main.activity_profile.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.ActivityProfileBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.util.*

class Profile : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var userDitails:DocumentReference=  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString())
    private lateinit var binding: ActivityProfileBinding
    @SuppressLint("MissingPermission", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val locationManagerr = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(locationManagerr.isLocationEnabled)
        {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                currentLocation:Location?->
                if (currentLocation!=null && Constant.isInternetOn(this))
                {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
                    val address = addresses!![0]
                    val state = address.adminArea
                    val country = address.countryName
                    val district = address.subAdminArea
                    val locationName = address.locality
                    val FullAddress = "$locationName,$district,$state,$country"
                    binding.locat.text=FullAddress
                }
            }.addOnFailureListener {
                exception->
                Toast.makeText(this, "Please Enable Your Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Please Enable Your Location", Toast.LENGTH_SHORT).show()
        }
        userDitails.get().addOnSuccessListener {
            if(it!=null)
            {
                val name:TextView=findViewById(R.id.uname)
                val age:TextView=findViewById(R.id.age)
                val Gender:TextView=findViewById(R.id.Gender)
                name.text=it.data?.get("fullname").toString()
                val dob=it.data?.get("dob").toString()
                val birthyear=dob.substring(5,9).toInt()
                var currentYear = LocalDate.now().year
                age.text = (currentYear-birthyear).toString()
                Gender.text=it.data?.get("gender").toString()
            }
        }


        val we:EditText=findViewById(R.id.edweight)
        val he:EditText=findViewById(R.id.edhight)
        var height:Double=0.0
        var weight:Double=0.0
        var Bmi:String="0"
        binding.add.setOnClickListener {
        if(he.text.toString()!="0" && we.text.toString()!="0"||he.text.toString()!="" && we.text.toString()!="") {
            weight=(we.text.toString()).toDouble()
            height=he.text.toString().toDouble()
            if(weight ==0.0 || height==0.0)
            {
                Bmi="0"
            }else {
                Bmi = Math.round(((weight / (height * height) * 1000.0) / 1000.0).toDouble())
                    .toString()
            }
        }
            val fitdata= mapOf(
                "weight" to we.text.toString(),
                "height" to he.text.toString(),
                "Bmi" to Bmi.toString()
            )
           userDitails.collection("fitness").document("fit").update(fitdata)
            finish()
        }

        val curruser=FirebaseAuth.getInstance().currentUser!!.uid
        val reference=Firebase.firestore.collection("user").document(curruser.toString())
        reference.collection("fitness").document("fit").get().addOnSuccessListener {
            if (it!=null)
            {
                val weightf= it.data?.get("weight").toString()
                val heightf=it.data?.get("height").toString()
                val bmif=it.data?.get("Bmi").toString()
                binding.edweight.setText(if(weightf!="")weightf.toString() else "0")
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