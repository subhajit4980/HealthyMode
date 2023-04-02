package com.example.HealthyMode.Service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.HealthyMode.Notification.NOTIFICATION_ID
import com.example.HealthyMode.Notification.Notification
import com.example.HealthyMode.Notification.messageExtra
import com.example.HealthyMode.Notification.titleExtra
import com.example.HealthyMode.Utils.Constant
import java.util.*

@SuppressLint("SpecifyJobSchedulerIdRange")
class WaterService : JobService() {
    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(p0: JobParameters?): Boolean {
        sheduleNotification(this@WaterService)

        val Interval = Constant.loadData(this!!, "reminder", "interval", "1")!!.toLong() * 3600000
        val jobScheduler = this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(2, ComponentName(this, WaterService::class.java))
            .setMinimumLatency(Interval).build()
        jobScheduler.schedule(jobInfo)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    @SuppressLint("LongLogTag", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    fun sheduleNotification(context: Context) {
        val check = Constant.loadData(context, "reminder", "check", "0")
        if (check == "1") {
            val intent = Intent(context, Notification::class.java)
            val title = "Time to drink water!"
            val message = "Reminder to drink a glass of water 💧💧💧now."
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val currentTimeMillis = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentTimeMillis
            val hourInMillis = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000
            val minuteInMillis = calendar.get(Calendar.MINUTE) * 60 * 1000
            val currentTime = hourInMillis + minuteInMillis
            val alarmManager =
                context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val startTime =
                Constant.loadData(context, "reminder", "startM", "32400000")!!.toLong()
            val endTime =
                Constant.loadData(context, "reminder", "endM", "79200000")!!.toLong()
            if (currentTime in startTime..endTime) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    currentTimeMillis,
                    pendingIntent
                )
            }
        }
    }

}