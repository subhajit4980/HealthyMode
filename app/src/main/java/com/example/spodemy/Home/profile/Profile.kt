package com.example.spodemy.Home.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.spodemy.data.fit
import com.example.spodemy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class Profile : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
        val state = addresses?.get(0)?.adminArea
        val country = addresses?.get(0)?.countryName
        val district = addresses?.get(0)?.subAdminArea
        val addr:String=district.toString()+","+state.toString()+","+country.toString()
        locat.text=addr.toString()
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance()
                .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
        this?.let {
            ref.get().addOnCompleteListener(it){
                    task->
                if(task.isSuccessful)
                {
                    val ds=task.result
                    val name: TextView =findViewById(R.id.uname)
                    name.text=ds.child("name").value.toString()
                    age.text=ds.child("age").value.toString()

                }
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
            Bmi = Math.round(((weight / (height * height)*1000.0)/1000.0).toDouble()).toString()
        }
            val ref: DatabaseReference =
                FirebaseDatabase.getInstance()
                    .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
            val fit = fit(he.text.toString(),we.text.toString(),Bmi.toString())
            ref.child("Fitness").setValue(fit)
                .addOnSuccessListener {
                    Toast.makeText(this, "Fitness Updated", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
        val Ref: DatabaseReference =
            FirebaseDatabase.getInstance()
                .getReference("Healthify/users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}/Fitness")
        this?.let {
            Ref.get().addOnCompleteListener(it){
                    task->
                if(task.isSuccessful)
                {
                    val ds=task.result
                    val weigthf:String=ds.child("weight").value.toString()
                    val heigthf:String=ds.child("height").value.toString()
                    val bmif:String=ds.child("bmi").value.toString()
                    edweight.setText(if(weigthf!="")weigthf.toString() else "0")
                    edhight.setText(if(heigthf!="")heigthf.toString() else "0")
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
        }
    }
}