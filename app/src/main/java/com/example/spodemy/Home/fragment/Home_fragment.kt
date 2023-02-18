package com.example.spodemy.Home.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.CircularPropagation
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide
import com.example.spodemy.Home.Food.Add_food
import com.example.spodemy.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_home_fragment.*
import kotlinx.android.synthetic.main.fragment_profile_fragment.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Suppress("SENSELESS_COMPARISON")
class Home_fragment : Fragment(),SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalsteps = 0f
    private var previoustotalstep = 0f
    var root:View?=null
    private var no_glass:Int?=null
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())
    private val handler = Handler()
    private var formattedTime:String?=null
//    @RequiresApi(Build.VERSION_CODES.O)
    private var curr_date:String= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().toString()
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    private lateinit var notification: Notification
    private val updatetimeRunnable=object :Runnable{
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat")
        override fun run() {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val min=calendar.get(Calendar.MINUTE)
            val sec=calendar.get(Calendar.SECOND)
//            curr_date= LocalDate.now().toString()
            formattedTime="$hour:$min:$sec"
            if(formattedTime=="0:0:0")
            {
                reset()
            }
            greeting_class()
            handler.postDelayed(this,1000)

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("SuspiciousIndentation", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         root=inflater.inflate(R.layout.fragment_home_fragment, container, false)
        handler.post(updatetimeRunnable)
        try{

            val curruser=FirebaseAuth.getInstance().currentUser!!.uid
            val ref=Firebase.firestore.collection("user").document(curruser.toString())
            ref.get().addOnSuccessListener {
                if (it!=null)
                {
                    val name= it.data!!.get("fullname").toString()
                    val uname:TextView=root!!.findViewById(R.id.name)
                    uname.text=name
                }
            }.addOnFailureListener {
                Toast.makeText(activity, "some thing went wrong", Toast.LENGTH_SHORT).show()
            }
//            greeting_class()
            existwater()
            addwater()
            addfood()
        loadData()
//        dataupload()
        var cpbar=root!!.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        cpbar.progressMax= 2000.0.toFloat()
//        resetsteps
            val sun:ImageView=root!!.findViewById(R.id.weather)
            sun.setOnClickListener {
                Toast.makeText(requireContext(), "$formattedTime", Toast.LENGTH_SHORT).show()
            }

            try {
                cpbar.setOnClickListener {
                    Toast.makeText(activity, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
                }
                cpbar.setOnLongClickListener{
                    reset()
                    true
                }

            }catch (e:Exception)
            {
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
            val walk:TextView=root!!.findViewById(R.id.walk)
            var rec_walk:String?=null
            rec_walk=walk.text.toString()
            if(rec_walk==null || rec_walk=="0")
            {
                val currsteps = totalsteps.toInt() - previoustotalstep.toInt()
                walk.text = "$currsteps"
                dataupload("$currsteps",LocalDate.now().toString())
                cpbar.progress=currsteps.toFloat()
            }
        }catch (e:Exception)
        {
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
        }

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return root
    }

    private fun greeting_class() {
        val time:ImageView=root!!.findViewById(R.id.weather)
        val greeting:TextView=root!!.findViewById(R.id.greeting)
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        if(hour.toInt() in 6..17)
        {
            time.setImageResource(R.drawable.sun)
            if(hour.toInt() in 6..12)
            {
                greeting.text="Good Morning !"
            }
            if(hour.toInt() in 13..14)
            {
                greeting.text="Good Noon !"
            }else if(hour.toInt() in 15..17){
                greeting.text="Good Afternoon !"
            }
        }else{
            time.setImageResource(R.drawable.moon)
            if(hour.toInt() in 18..19)
            {
                greeting.text="Good Evening !"
            }
            else{
                greeting.text="Good Night !"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updatetimeRunnable)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addwater() {
        userDitails.collection("water track").document(LocalDate.now().toString()).get().addOnSuccessListener {
            if (it!=null)
            {
                val glass: String = it.data?.get("glass").toString()
                try {
                    if(glass == ""||glass==null || glass.isEmpty())
                    {
                        no_glass=0
                    }else no_glass=glass.toInt()
                }catch (_:Exception)
                {
                    no_glass=0
                }
            }
        }

        val addwater:AppCompatButton=root!!.findViewById(R.id.addwater)
        addwater.setOnClickListener {
            deletewater.isClickable=true
            deletewater.setBackgroundResource(R.drawable.baseline_remove_circle_outline_24)
            no_glass = no_glass?.plus(1)
            val curr_date = LocalDate.now()
            val water= mapOf(
                "Date" to curr_date.toString(),
                "glass" to no_glass.toString()
            )
            userDitails.collection("water track").document(curr_date.toString()).set(water)
            updatewater()
        }
        val reduceWater:AppCompatButton=root!!.findViewById(R.id.deletewater)
        reduceWater.setOnClickListener {
            if(water_level.text.toString()=="0")
            {
                deletewater.isClickable=false
                deletewater.setBackgroundResource(R.drawable.disable_remove)
            }else{
            no_glass = no_glass?.minus(1)
            val curr_date = LocalDate.now()
            val water= mapOf(
                "Date" to curr_date.toString(),
                "glass" to no_glass.toString()
            )
            userDitails.collection("water track").document(curr_date.toString()).set(water)
            updatewater()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun reset() {
        val cpbar=root!!.findViewById<CircularProgressBar>(R.id.circularProgressBar)
            previoustotalstep = totalsteps
            val walk:TextView=root!!.findViewById(R.id.walk)
            walk.text = "0"
            dataupload("0",LocalDate.now().toString())
            savedata()
            cpbar.apply {
                setProgressWithAnimation(0.toFloat())
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dataupload(currsteps:String,curr_date:String)
    {
        val steps= hashMapOf(
            "steps" to currsteps.toString(),
            "date" to curr_date.toString()
        )
        val curruser=FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.firestore.collection("user").document(curruser.toString())
            .collection("steps").document(curr_date.toString()).set(steps)
    }
    override fun onResume() {
        super.onResume()

        try{
            running = true
            val stepsensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            if (stepsensor == null) {
                Toast.makeText(requireContext(), "sensor not working", Toast.LENGTH_SHORT).show()
            } else {
                sensorManager!!.registerListener(this, stepsensor, SensorManager.SENSOR_DELAY_UI)
            }
        }catch (e:Exception)
        {
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        try{
            if (running) {
                totalsteps = event!!.values[0]
                val currsteps = totalsteps.toInt() - previoustotalstep.toInt()
                walk.text = "$currsteps"
                dataupload(currsteps.toString(),LocalDate.now().toString())
                val cpbar:CircularProgressBar=root!!.findViewById(R.id.circularProgressBar)
                cpbar.apply {
                    setProgressWithAnimation(currsteps.toFloat())
                }
            }
        }catch (e:Exception)
        {
            Log.e("TAG",e.message.toString())
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun loadData() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val saveNumber: Float = sharedPreferences.getFloat("key1", 0f)
        Log.d("mainActivity", "$saveNumber")
        previoustotalstep = saveNumber
    }
    private fun savedata() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("key1", previoustotalstep)
        editor.apply()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun existwater()
    {
        val curr_date = LocalDate.now()
        val water= mapOf(
            "Date" to curr_date.toString(),
            "glass" to "0"
        )
        userDitails.collection("water track").document(curr_date.toString()).get().addOnSuccessListener {
                snapshot->
            if (!snapshot.exists())
            {
                userDitails.collection("water track").document(curr_date.toString()).set(water)
                updatewater()
            }else
            {
                updatewater()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatewater()
    {
        userDitails.collection("water track").document(curr_date.toString()).get().addOnSuccessListener {
            if (it!=null)
            {
                val gls=it.data?.get("glass").toString()
                val water_level:TextView=root!!.findViewById(R.id.water_level)
                water_level.text=gls.toString()
            }
        }
    }
    private fun addfood() {
        val add:ImageView=root!!.findViewById(R.id.add_food)
        add.setOnClickListener {
            val intent=Intent(activity,Add_food::class.java)
            startActivity(intent)
        }
    }
}
