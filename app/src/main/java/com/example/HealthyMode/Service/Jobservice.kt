package com.example.HealthyMode.Service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.Utils.Constant.loadData
import com.example.HealthyMode.Utils.Constant.savedata
import java.time.LocalDate
import java.util.*


@SuppressLint("SpecifyJobSchedulerIdRange")
class Jobservice : JobService() {
    private val interval = 10000L

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(event: JobParameters?): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        val sec = calendar.get(Calendar.SECOND)
        val today = LocalDate.now().dayOfMonth.toString()
        val day = loadData(this, "day", "today", today)
        if ("$hour:$min" == "0:0" || day != today) {
            val pre_step = loadData(this, "step_count", "total_step", "0")!!.toInt()
            Constant.savedata(this, "step_count", "previous_step", pre_step.toString())
            val step = Constant.loadData(this, "step_count", "total_step", "0")
            val previoustotalstep =
                Constant.loadData(this, "step_count", "previous_step", "0")!!.toInt() ?: 0
            val c_step = Math.abs(step!!.toInt() - previoustotalstep)
            val target = Constant.loadData(this, "myPrefs", "target", "1000").toString()
            val notification = NotificationCompat.Builder(this, "Stepcount")
                .setContentTitle("Tracking steps...")
                .setContentText("Current Steps : $c_step  Target Steps:$target")
                .setSmallIcon(R.drawable.mainlogo)
                .setOngoing(true).setSilent(true)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.notify(1, notification!!.build())
            startForeground(1, notification!!.build())
        } else {
            savedata(this, "day", "today", today)
        }
        shedulejob()
        return true
    }

    private fun shedulejob() {
        val jobsh = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobinfo = JobInfo.Builder(1, ComponentName(this, Jobservice::class.java))
            .setMinimumLatency(interval).build()
        jobsh.schedule(jobinfo)

    }

    override fun onStopJob(event: JobParameters?): Boolean {

        return true
    }
}