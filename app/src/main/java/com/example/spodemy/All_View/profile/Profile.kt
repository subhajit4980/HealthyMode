package com.example.spodemy.All_View.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.spodemy.Authentication_Asset.signup_fragment.Companion.TAG
import com.example.spodemy.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class Profile : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var userDitails:DocumentReference=  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val locationManagerr = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(locationManagerr.isLocationEnabled)
        {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                currentLocation:Location?->
                if (currentLocation!=null)
                {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
                    val address = addresses!![0]
                    val state = address.adminArea
                    val country = address.countryName
                    val district = address.subAdminArea
                    val locationName = address.locality
                    val addr: String =
                        "$locationName,$district,$state,$country"
                    locat.text=addr.toString()
                }
            }.addOnFailureListener {
                exception->
                Log.d(TAG,"failed to get location: $exception")
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
                age.text=it.data?.get("age").toString()
                Gender.text=it.data?.get("gender").toString()
            }
        }


        val we:EditText=findViewById(R.id.edweight)
        val he:EditText=findViewById(R.id.edhight)
        var height:Double=0.0
        var weight:Double=0.0
        var Bmi:String="0"
        add.setOnClickListener {
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
//            val ref: DatabaseReference =
//                FirebaseDatabase.getInstance()
//                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
//            val fit = fit(he.text.toString(),we.text.toString(),Bmi.toString())
//            ref.child("Fitness").setValue(fit)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "Fitness Updated", Toast.LENGTH_SHORT).show()
//                }
            val fitdata= mapOf(
                "weight" to we.text.toString(),
                "height" to he.text.toString(),
                "Bmi" to Bmi.toString()
            )
           userDitails.collection("fitness").document("fit").update(fitdata)
            finish()
        }

        val curruser=FirebaseAuth.getInstance().currentUser!!.uid
        val reference=Firebase.firestore.collection("user").document(curruser.toString()).collection("fitness").document("fit")
        reference.get().addOnSuccessListener {
            if (it!=null)
            {
                val weightf= it.data?.get("weight").toString()
                val heightf=it.data?.get("height").toString()
                val bmif=it.data?.get("Bmi").toString()
                edweight.setText(if(weightf!="")weightf.toString() else "0")
                edhight.setText(if(heightf!="")heightf.toString() else "0")
                bmi.text=if(bmif!="")bmif.toString() else "0"
                var bm:Double=0.0
                if(bmif.isEmpty() || bmif=="")
                {
                    bm=0.0
                }else {
                    bm = bmif?.toDouble()!!
                }
                if(bm<18.5)
                {
                    measure.text="You are underweight"
                    measure.setBackgroundColor(Color.RED)
                }
                else if(bm<29.9 && bm>25.0)
                {
                    measure.text="You are Overweight"
                    measure.setBackgroundColor(Color.RED)
                }
                else if(bm>30.0)
                {
                    measure.text="You are Obese Range"
                    measure.setBackgroundColor(Color.YELLOW)
                }else{
                    measure.text="You are Normal and Healthy"
                    measure.setBackgroundColor(Color.GREEN)
                }
            }
        }


//        val Ref: DatabaseReference =
//            FirebaseDatabase.getInstance()
//                .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/Fitness")
//        this?.let {
//            Ref.get().addOnCompleteListener(it){
//                    task->
//                if(task.isSuccessful)
//                {
//                    val ds=task.result
//                    val weigthf:String=ds.child("weight").value.toString()
//                    val heigthf:String=ds.child("height").value.toString()
//                    val bmif:String=ds.child("bmi").value.toString()
//                    edweight.setText(if(weigthf!="")weigthf.toString() else "0")
//                    edhight.setText(if(heigthf!="")heigthf.toString() else "0")
//                    bmi.text=if(bmif!="")bmif.toString() else "0"
//                    var bm:Double=0.0
//                    if(bmif.isEmpty() || bmif=="")
//                    {
//                        bm=0.0
//                    }else {
//                        bm = bmif?.toDouble()!!
//                    }
//                    if(bm<18.5)
//                    {
//                        measure.text="You are underweight"
//                        measure.setBackgroundColor(Color.RED)
//                    }
//                    else if(bm<29.9 && bm>25.0)
//                    {
//                        measure.text="You are Overweight"
//                        measure.setBackgroundColor(Color.RED)
//                    }
//                    else if(bm>30.0)
//                    {
//                        measure.text="You are Obese Range"
//                        measure.setBackgroundColor(Color.YELLOW)
//                    }else{
//                        measure.text="You are Normal and Healthy"
//                        measure.setBackgroundColor(Color.GREEN)
//                    }
//                }
//            }
//        }
    }


}