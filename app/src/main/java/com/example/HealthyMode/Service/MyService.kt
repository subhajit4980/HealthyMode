package com.example.HealthyMode.Service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Math.abs
import java.time.LocalDate
import java.util.*

class MyService : Service(), SensorEventListener {
    private var running = false
    private var totalsteps = 0
    private var step: String? = "400"
    private lateinit var sensorManager: SensorManager
    private lateinit var steocounterListener: SensorEventListener
    private lateinit var notification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(steocounterListener)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun start() {
        try {
            step_count()
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
        val step = Constant.loadData(this, "step_count", "total_step", "0")
        val previoustotalstep =
            Constant.loadData(this, "step_count", "previous_step", "0")!!.toInt() ?: 0
        val c_step = abs(step!!.toInt() - previoustotalstep!!)
        val target = Constant.loadData(this, "myPrefs", "target", "1000").toString()
        if (Constant.isInternetOn(applicationContext)) {
            dataupload(c_step.toString(), LocalDate.now().toString())
        }

        notification = NotificationCompat.Builder(this, "Stepcount")
            .setContentTitle("Tracking steps...")
            .setContentText("Current Steps : $c_step  Target Steps:$target")
            .setSmallIcon(R.drawable.mainlogo)
            .setOngoing(true)
            .setSilent(true)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.notify(1, notification!!.build())
        startForeground(1, notification!!.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    private fun step_count() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val stepsensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsensor == null) {
            Toast.makeText(this, "sensor not working", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager.registerListener(this, stepsensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        val total_steps = event!!.values[0]
        Constant.savedata(this, "step_count", "total_step", total_steps.toInt().toString())
        val previoustotalstep =
            Constant.loadData(this, "step_count", "previous_step", "0")!!.toInt() ?: 0
        totalsteps=Constant.loadData(this, "step_count", "total_step", "0")!!.toInt() ?: 0
        val currsteps = totalsteps.toInt() - previoustotalstep!!
//        step = "$currsteps"
        val target = Constant.loadData(this, "myPrefs", "target", "1000").toString()
        val curr_date = LocalDate.now().toString()
        if (Constant.isInternetOn(applicationContext)) {
            dataupload(currsteps.toString(), curr_date)
        }
        notificationManager!!.notify(1,
            notification!!.setContentText("Current Steps : $currsteps  Target Steps:$target")
                .build()
        )
        startForeground(1, notification!!.build())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun dataupload(currsteps: String, curr_date: String) {
        val steps = hashMapOf(
            "steps" to currsteps.toString(),
            "date" to curr_date.toString()
        )
        val curruser = FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.firestore.collection("user").document(curruser.toString())
            .collection("steps").document(curr_date.toString()).set(steps)
    }

}