package com.example.HealthyMode.All_View.Reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.HealthyMode.Notification.*
import com.example.HealthyMode.R
import com.example.HealthyMode.Service.WaterService
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.FragmentReminderBinding
import java.time.*
import java.util.*

class Reminder : AppCompatActivity() {
    private lateinit var binding: FragmentReminderBinding
    private  var startTime:Long=32400000
    private  var endTime:Long=79200000
    private  var start="9:00 AM"
    private  var end="10:00 PM"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotification()
        startTime=Constant.loadData(this, "reminder", "startM","32400000")!!.toLong()
        endTime=Constant.loadData(this, "reminder", "endM","79200000")!!.toLong()
        binding.interval.minValue = 1
        binding.interval.maxValue = 24
        UI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        val name = "channel"
        val message = "message"
        val important = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channel_ID, name, important)
        channel.description = message
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun UI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.backward.setOnClickListener {
            finish()
        }
        val Interval = Constant.loadData(this, "reminder", "interval", "1")
        binding.interval.value = Interval!!.toInt()
        binding.start.setText(Constant.loadData(this,"reminder","start","09:00AM"))
        binding.end.setText(Constant.loadData(this,"reminder","end","10:00 PM"))
        var check = Constant.loadData(this, "reminder", "check", "0")
        if (check == "1") {
            binding.switchMaterial.isChecked = true
            binding.switchMaterial.thumbTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_orange))
        } else {
            binding.switchMaterial.thumbTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_grey))
        }
        binding.switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.switchMaterial.thumbTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_orange))
                Constant.savedata(this, "reminder", "check", "1")
                check="1"
            } else {
                binding.switchMaterial.thumbTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_grey))
                Constant.savedata(this, "reminder", "check", "0")
                check="0"
            }
        }
        binding.start.setOnClickListener{
            Constant.showTimePicker(binding.start,this,object : Constant.OnTimeSelectedListener {
                override fun onTimeSelected(timeInMillis: Long,Time:String) {
                    // Do something with the selected time in milliseconds
                    startTime=timeInMillis
                    start=Time
                }
            })

        }
        binding.end.setOnClickListener{
            Constant.showTimePicker(binding.end,this,object : Constant.OnTimeSelectedListener {
                override fun onTimeSelected(timeInMillis: Long,Time:String) {
                    endTime=timeInMillis
                    end=Time
                }
            })

        }
        binding.save.setOnClickListener{
            if(check=="1")
            {
                Constant.savedata(applicationContext, "reminder", "start", start)
                Constant.savedata(applicationContext, "reminder", "end",end)
                Constant.savedata(applicationContext, "reminder", "startM",startTime.toString())
                Constant.savedata(applicationContext, "reminder", "endM",endTime.toString())
                Constant.savedata(this, "reminder", "interval", (binding.interval.value).toString())
                Toast.makeText(this, "Reminder  set", Toast.LENGTH_SHORT).show()
               val Interval = (binding.interval.value*3600000).toLong()
                val jobScheduler=this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                val jobInfo= JobInfo.Builder(2, ComponentName(this, WaterService::class.java)).setMinimumLatency(Interval).build()
                jobScheduler.schedule(jobInfo)
            }else{
                Toast.makeText(this, "Reminder not set", Toast.LENGTH_SHORT).show()
            }
        }
    }

}