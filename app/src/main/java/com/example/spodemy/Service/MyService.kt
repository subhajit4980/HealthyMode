package com.example.spodemy.Service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.spodemy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home_fragment.*
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

class MyService:Service(),SensorEventListener {
    private var running=false
    private var totalsteps = 0f
    private var step:String?="400"
    private  var sensorManager: SensorManager?=null
    private  var steocounterListener: SensorEventListener?=null
    private var notification:NotificationCompat.Builder?=null
    private var notificationManager:NotificationManager?=null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
        sensorManager!!.unregisterListener(steocounterListener)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action)
        {
            ACTION_START->start()
            ACTION_STOP->stop()
        }
        return START_STICKY
    }
    private fun start()
    {
        try {

        step_count()
        }catch (e:Exception)
        {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
        val step=loadData("step_count","total_step","0")
        val previoustotalstep=loadData("step_count","previous_step","0")!!.toInt()?:0
        val c_step=step!!.toInt()- previoustotalstep!!
        val target=loadData("myPrefs","target","1000").toString()
        notification = NotificationCompat.Builder(this, "Stepcount")
            .setContentTitle("Tracking steps...")
            .setContentText("Current Steps : $c_step  Target Steps:$target")
            .setSmallIcon(R.drawable.mainlogo)
            .setOngoing(true)
         notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.notify(1, notification!!.build())
        startForeground(1, notification!!.build())
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val min=calendar.get(Calendar.MINUTE)
//        val sec=calendar.get(Calendar.SECOND)
////            curr_date= LocalDate.now().toString()
//        val formattedTime="$hour:$min:$sec"
//        if(formattedTime=="0:0:0")
//        {
//            stopForeground(true)
//            stopSelf()
//        }
    }
    private fun stop() {
        stopForeground(true)
        stopSelf()
    }
    private fun step_count() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val stepsensor: Sensor? = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsensor == null) {
            Toast.makeText(this, "sensor not working", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager!!.registerListener(this, stepsensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        }
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        totalsteps = event!!.values[0]
         val previoustotalstep=loadData("step_count","previous_step","0")!!.toInt()?:0
        val currsteps = totalsteps.toInt()- previoustotalstep!!
        step = "$currsteps"
        savedata("step_count","total_step",totalsteps.toInt().toString())
        val target=loadData("myPrefs","target","1000").toString()
        val curr_date=LocalDate.now().toString()
        if (isInternetOn())
        {
            dataupload(currsteps.toString(),curr_date)
        }
        notificationManager!!.notify(1,notification!!.setContentText("Current Steps : $currsteps  Target Steps:$target").build())
        startForeground(1,notification!!.build())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    fun loadData(pre_nmae:String,key:String,default:String): String? {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val saveNumber: String? = sharedPreferences.getString(key, default)
        Log.d("mainActivity", "$saveNumber")
        return saveNumber
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun savedata(pre_nmae:String,key:String,value:String) {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dataupload(currsteps:String,curr_date:String)
    {
        val steps= hashMapOf(
            "steps" to currsteps.toString(),
            "date" to curr_date.toString()
        )
        val curruser= FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.firestore.collection("user").document(curruser.toString())
            .collection("steps").document(curr_date.toString()).set(steps)
    }
    fun isInternetOn():Boolean{
        val conncectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netinfo=conncectivityManager.activeNetworkInfo
        return netinfo!=null && netinfo.isConnected
    }
}