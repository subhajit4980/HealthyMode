package com.example.HealthyMode.UI.Reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.HealthyMode.Notification.Notification
import com.example.HealthyMode.Notification.messageExtra
import com.example.HealthyMode.Notification.titleExtra
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.Utils.Constant.breakfastmessage
import com.example.HealthyMode.Utils.Constant.breakfasttitle
import com.example.HealthyMode.Utils.Constant.currentDate
import com.example.HealthyMode.Utils.Constant.dinnermessage
import com.example.HealthyMode.Utils.Constant.dinnertitle
import com.example.HealthyMode.Utils.Constant.eveningmessage
import com.example.HealthyMode.Utils.Constant.eveningsnacktitle
import com.example.HealthyMode.Utils.Constant.loadData
import com.example.HealthyMode.Utils.Constant.lunchmessage
import com.example.HealthyMode.Utils.Constant.lunchtitle
import com.example.HealthyMode.Utils.Constant.moriningsnackmessage
import com.example.HealthyMode.Utils.Constant.morningsnacktitle
import com.example.HealthyMode.databinding.MealReminderBinding


class MealReminder : AppCompatActivity() {
    private lateinit var binding: MealReminderBinding
    private var breakfastTime: Long = 32400000
    private var breakfastTimestamp = "09:00AM"
    private var morningsnackTime: Long = 32400000
    private var morningsnackTimestamp = "09:00AM"
    private var lunchTime: Long = 32400000
    private var lunchTimestamp = "09:00AM"
    private var eveningsanckTime: Long = 32400000
    private var eveningsanckTimestamp = "09:00AM"
    private var dinnerTime: Long = 32400000
    private var dinnerTimestamp = "09:00AM"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MealReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotification()
        UIupdate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun UIupdate() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.backward.setOnClickListener {
            finish()
        }
        breakfastTime = loadData(this, "Meal", "breakfastTime", "32400000")!!.toLong()
        morningsnackTime = loadData(this, "Meal", "morningsnackTime", "32400000")!!.toLong()
        lunchTime = loadData(this, "Meal", "lunchTime", "32400000")!!.toLong()
        eveningsanckTime = loadData(this, "Meal", "eveningsanckTime", "32400000")!!.toLong()
        dinnerTime = loadData(this, "Meal", "dinnerTime", "32400000")!!.toLong()
        var Breakfastcheck = loadData(this@MealReminder, "Meal", "breakfastcheck", "0")
        var morningsanckcheck = loadData(this@MealReminder, "Meal", "morningsanckcheck", "0")
        var lunchcheck = loadData(this@MealReminder, "Meal", "lunchcheck", "0")
        var eveningsnackcheck = loadData(this@MealReminder, "Meal", "eveningsnackcheck", "0")
        var Dinnercheck = loadData(this@MealReminder, "Meal", "dinnercheck", "0")
        binding.apply {
            breakfastTimestamp =
                loadData(this@MealReminder, "Meal", "breakfastTimestamp", "09:00AM").toString()
            start.setText(breakfastTimestamp)
            morningsnackTimestamp = loadData(
                this@MealReminder,
                "Meal",
                "morningsnackTimestamp",
                "09:00AM"
            ).toString()
            MSnackstart.setText(morningsnackTimestamp)
            lunchTimestamp =
                loadData(this@MealReminder, "Meal", "lunchTimestamp", "09:00AM").toString()
            Lunchstart.setText(lunchTimestamp)
            eveningsanckTimestamp = loadData(
                this@MealReminder,
                "Meal",
                "eveningsanckTimestamp",
                "09:00AM"
            ).toString()
            EVsnackstart.setText(
                eveningsanckTimestamp
            )
            dinnerTimestamp =
                loadData(this@MealReminder, "Meal", "dinnerTimestamp", "09:00AM").toString()
            dinnerstart.setText(dinnerTimestamp)
            start.setOnClickListener {
                Constant.showTimePicker(
                    binding.start,
                    this@MealReminder,
                    object : Constant.OnTimeSelectedListener {
                        override fun onTimeSelected(timeInMillis: Long, Time: String) {
                            breakfastTime = timeInMillis
                            breakfastTimestamp = Time
                        }
                    })
            }
            MSnackstart.setOnClickListener {
                Constant.showTimePicker(
                    binding.MSnackstart,
                    this@MealReminder,
                    object : Constant.OnTimeSelectedListener {
                        override fun onTimeSelected(timeInMillis: Long, Time: String) {
                            morningsnackTime = timeInMillis
                            morningsnackTimestamp = Time
                        }
                    })
            }
            Lunchstart.setOnClickListener {
                Constant.showTimePicker(
                    binding.Lunchstart,
                    this@MealReminder,
                    object : Constant.OnTimeSelectedListener {
                        override fun onTimeSelected(timeInMillis: Long, Time: String) {
                            lunchTime = timeInMillis
                            lunchTimestamp = Time
                        }
                    })
            }
            EVsnackstart.setOnClickListener {
                Constant.showTimePicker(
                    binding.EVsnackstart,
                    this@MealReminder,
                    object : Constant.OnTimeSelectedListener {
                        override fun onTimeSelected(timeInMillis: Long, Time: String) {
                            eveningsanckTime = timeInMillis
                            eveningsanckTimestamp = Time
                        }
                    })
            }
            dinnerstart.setOnClickListener {
                Constant.showTimePicker(
                    binding.dinnerstart,
                    this@MealReminder,
                    object : Constant.OnTimeSelectedListener {
                        override fun onTimeSelected(timeInMillis: Long, Time: String) {
                            dinnerTime = timeInMillis
                            dinnerTimestamp = Time
                        }
                    })
            }
            var check = loadData(this@MealReminder, "Meal", "check", "0")
            if (check == "1") {
                binding.switchMaterial.isChecked = true
                binding.switchMaterial.thumbTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@MealReminder,
                            R.color.dark_orange
                        )
                    )
            } else {
                binding.switchMaterial.thumbTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@MealReminder,
                            R.color.dark_grey
                        )
                    )
            }
            switchMaterial.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.switchMaterial.thumbTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@MealReminder,
                                R.color.dark_orange
                            )
                        )
                    check = "1"
                } else {
                    binding.switchMaterial.thumbTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@MealReminder,
                                R.color.dark_grey
                            )
                        )
                    check = "0"
                }
            }
            breakfastcheck.isChecked = Breakfastcheck == "1"
            MSnackcheck.isChecked = morningsanckcheck == "1"
            Lunchcheck.isChecked = lunchcheck == "1"
            EVsnackcheck.isChecked = eveningsnackcheck == "1"
            dinnercheck.isChecked = Dinnercheck == "1"
            breakfastcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) Breakfastcheck = "1"
                else Breakfastcheck = "0"
            }
            MSnackcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) morningsanckcheck = "1"
                else morningsanckcheck = "0"
            }
            Lunchcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) lunchcheck = "1"
                else lunchcheck = "0"
            }
            EVsnackcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) eveningsnackcheck = "1"
                else eveningsnackcheck = "0"
            }
            dinnercheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) Dinnercheck = "1"
                else Dinnercheck = "0"
            }
            save.setOnClickListener {
                if (check == "1") {
                    Constant.savedata(this@MealReminder, "Meal", "check", "1")
                    if (Breakfastcheck == "1") {
                        ScheduleAlarm(breakfasttitle, breakfastmessage, breakfastTime, 12)
                        Constant.savedata(this@MealReminder, "Meal", "breakfastcheck", "1")
                        Constant.savedata(
                            this@MealReminder,
                            "Meal",
                            "breakfastTimestamp",
                            breakfastTimestamp
                        )
                    } else {
                        cancelAlarm(12)
                        Constant.savedata(this@MealReminder, "Meal", "breakfastcheck", "0")
                    }
                    if (morningsanckcheck == "1") {
                        ScheduleAlarm(morningsnacktitle, moriningsnackmessage, morningsnackTime, 13)
                        Constant.savedata(this@MealReminder, "Meal", "morningsanckcheck", "1")
                        Constant.savedata(
                            this@MealReminder,
                            "Meal",
                            "morningsnackTimestamp",
                            morningsnackTimestamp
                        )
                    } else {
                        cancelAlarm(13)
                        Constant.savedata(this@MealReminder, "Meal", "morningsanckcheck", "0")
                    }
                    if (lunchcheck == "1") {
                        ScheduleAlarm(lunchtitle, lunchmessage, lunchTime, 14)
                        Constant.savedata(this@MealReminder, "Meal", "lunchcheck", "1")
                        Constant.savedata(
                            this@MealReminder,
                            "Meal",
                            "lunchTimestamp",
                            lunchTimestamp
                        )
                    } else {
                        cancelAlarm(14)
                        Constant.savedata(this@MealReminder, "Meal", "lunchcheck", "0")
                    }
                    if (eveningsnackcheck == "1") {
                        ScheduleAlarm(eveningsnacktitle, eveningmessage, eveningsanckTime, 15)
                        Constant.savedata(this@MealReminder, "Meal", "eveningsnackcheck", "1")
                        Constant.savedata(
                            this@MealReminder,
                            "Meal",
                            "eveningsanckTimestamp",
                            eveningsanckTimestamp
                        )
                    } else {
                        cancelAlarm(15)
                        Constant.savedata(this@MealReminder, "Meal", "eveningsnackcheck", "0")
                    }
                    if (Dinnercheck == "1") {
                        ScheduleAlarm(dinnertitle, dinnermessage, dinnerTime, 16)
                        Constant.savedata(this@MealReminder, "Meal", "dinnercheck", "1")
                        Constant.savedata(
                            this@MealReminder,
                            "Meal",
                            "dinnerTimestamp",
                            dinnerTimestamp
                        )
                    } else {
                        cancelAlarm(16)
                        Constant.savedata(this@MealReminder, "Meal", "dinnercheck", "0")
                    }
                } else {
                    cancelAlarm(12)
                    cancelAlarm(13)
                    cancelAlarm(14)
                    cancelAlarm(15)
                    cancelAlarm(16)
                    Constant.savedata(this@MealReminder, "Meal", "check", "0")
                    Constant.savedata(this@MealReminder, "Meal", "breakfastcheck", "0")
                    Constant.savedata(this@MealReminder, "Meal", "lunchcheck", "0")
                    Constant.savedata(this@MealReminder, "Meal", "morningsanckcheck", "0")
                    Constant.savedata(this@MealReminder, "Meal", "eveningsnackcheck", "0")
                    Constant.savedata(this@MealReminder, "Meal", "dinnercheck", "0")
                }
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {
        val name = "MealsNotification"
        val message = "message"
        val important = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(Constant.Mealchannelid, name, important)
        channel.description = message
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("LongLogTag", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun ScheduleAlarm(title: String, message: String, time: Long, notificationID: Int) {
        val intent = Intent(applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (System.currentTimeMillis() >= time + currentDate()) {
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time + currentDate() + 86400000,
                    INTERVAL_DAY,
                    pendingIntent
                )
                Log.d("TAG-------------------------------> ", "alarm SET successfully")
            } else {
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    time + currentDate(),
                    INTERVAL_DAY,
                    pendingIntent
                )
                Log.d("TAG-------------------------------> ", "alarm SET successfully")
            }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "LongLogTag")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun cancelAlarm(notificationID: Int) {
        val intent = Intent(applicationContext, Notification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            Log.d("TAG-------------------------------> ", "alarm cancel")
        } else {
            Log.d("TAG-------------------------------> ", "alarm does not have pending intent")
        }
    }
}