package com.example.spodemy.All_View.fragment

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.airbnb.lottie.LottieAnimationView
import com.example.spodemy.All_View.Food.Add_food
import com.example.spodemy.R
import com.example.spodemy.ResetStepCountWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_home_fragment.*
import kotlinx.android.synthetic.main.fragment_profile_fragment.view.*
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Suppress("SENSELESS_COMPARISON")
class Home_fragment : Fragment() {
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
            stepCounter()
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

            userDitails.get().addOnSuccessListener {
                if(it!=null)
                {
                    val uname:TextView=root!!.findViewById(R.id.name)
                    uname.text=it.data!!.get("fullname").toString()
                }
            }
            set_target()
            existwater()
            addwater()
            addfood()
        var cpbar=root!!.findViewById<CircularProgressBar>(R.id.circularProgressBar)
//            reset step counter
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
//            when fragment change control step counter
//            val walk:TextView=root!!.findViewById(R.id.walk)
//            var rec_walk:String?=null
//            rec_walk=walk.text.toString()
//            if(rec_walk==null || rec_walk=="0")
//            {
//                val currsteps = totalsteps.toInt() - previoustotalstep.toInt()
//                walk.text = "$currsteps"
//                dataupload("$currsteps",LocalDate.now().toString())
//                cpbar.progress=currsteps.toFloat()
//            }
            ////////////////////////////////////////////


        }catch (e:Exception)
        {
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
        }
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return root
    }

    ///////////////////////////////////////////////


    private fun backgroundreset() {
        val workManager = context?.let { WorkManager.getInstance(it) }
        val midnight = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis

        val resetStepCountRequest = OneTimeWorkRequestBuilder<ResetStepCountWorker>()
            .setInitialDelay(midnight - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        workManager?.enqueue(resetStepCountRequest)
    }
// greeting user
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
//    control daily water intake
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
            if(no_glass==10)
            {
                val cong: LottieAnimationView =root!!.findViewById(R.id.animation_view)
                cong.visibility=View.VISIBLE
                animation_view2.visibility=View.VISIBLE
                cong.playAnimation()
                animation_view2.playAnimation()
                Handler().postDelayed({
                    cong.visibility=View.GONE
                    cong.cancelAnimation()
                    animation_view2.visibility=View.GONE
                    animation_view2.cancelAnimation()},2000)
            }
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

// reset the steps
    @RequiresApi(Build.VERSION_CODES.O)
    private fun reset() {
//        val cpbar=root!!.findViewById<CircularProgressBar>(R.id.circularProgressBar)
//            previoustotalstep = totalsteps
//            val walk:TextView=root!!.findViewById(R.id.walk)
//            walk.text = "0"
//            dataupload("0",LocalDate.now().toString())
//            savedata()
//            cpbar.apply {
//                setProgressWithAnimation(0.toFloat())
//            }
        val pre_step=loadData("step_count","total_step","0")!!.toInt()
        savedata("step_count","previous_step",pre_step.toString())
        stepCounter()
         dataupload("0",LocalDate.now().toString())
    }
//    upload steps data in firestore
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
//    update water level at UI textview
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
//    add your daily meal
    private fun addfood() {
        val add:ImageView=root!!.findViewById(R.id.add_food)
        add.setOnClickListener {
            val intent=Intent(activity,Add_food::class.java)
            startActivity(intent)
        }
    }
    fun set_target()
    {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val target=sharedPreferences.getString("target","1000")
        val cpbar=root!!.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        cpbar.progressMax= target!!.toFloat()
        val goal:TextView=root!!.findViewById(R.id.goal)
        goal.text=target.toString()
    }
//    fun cong()
//    {   val waterlevel:TextView=root!!.findViewById(R.id.water_level)
//        if(waterlevel.text.toString()=="10")
//        {
//            val cong: LottieAnimationView =root!!.findViewById(R.id.animation_view)
//            cong.visibility=View.VISIBLE
//            cong.playAnimation()
//            Handler().postDelayed({cong.visibility=View.GONE
//            cong.cancelAnimation()},10000)
//        }
//    }
    fun stepCounter()
{
//    try{
        val t_step=loadData("step_count","total_step","0").toString()
        val pre_step=loadData("step_count","previous_step","0").toString()
        val curr_step= abs(t_step.toInt()-pre_step.toInt()).toString()
        val walk:TextView=root!!.findViewById(R.id.walk)
        walk.text=curr_step.toString()
        val cpbar=root!!.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        cpbar.progress= curr_step.toFloat()
//    }catch(e:java.lang.Exception) {
//        android.widget.Toast.makeText(requireActivity(), e.message.toString(), Toast.LENGTH_SHORT).show()
//    }


    }
fun loadData(pre_nmae:String,key:String,default:String): String? {
    val sharedPreferences: SharedPreferences =
        requireActivity().getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
    val saveNumber: String? = sharedPreferences.getString(key, default)
    Log.d("mainActivity", "$saveNumber")
    return saveNumber
}
    @RequiresApi(Build.VERSION_CODES.O)
    private fun savedata(pre_nmae:String,key:String,value:String) {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
}

