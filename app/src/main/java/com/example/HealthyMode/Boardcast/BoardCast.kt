package com.example.HealthyMode.Boardcast

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.HealthyMode.Service.MyService
import com.example.HealthyMode.Service.WaterService
import com.example.HealthyMode.Utils.Constant

class BoardCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_REBOOT) {
            var wcheck = Constant.loadData(context!!, "Sanitizerreminder", "check", "0")
            var scheck = Constant.loadData(context, "Sanitizerreminder", "check", "0")
            val Interval =
                Constant.loadData(context, "reminder", "interval", "1")!!.toLong() * 3600000
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(2, ComponentName(context, WaterService::class.java))
                .setMinimumLatency(Interval).build()
            if (wcheck == "1") {
                jobScheduler.schedule(jobInfo)
            }
            val SInterval = Constant.loadData(context, "Sanitizerreminder", "interval", "1")!!
                .toLong() * 3600000
            val SjobInfo = JobInfo.Builder(2, ComponentName(context, WaterService::class.java))
                .setMinimumLatency(SInterval).build()
            if (scheck == "1") {
                jobScheduler.schedule(SjobInfo)
            }
//            val jobinfo = JobInfo.Builder(1, ComponentName(context, Step_reset_service::class.java))
//                .setMinimumLatency(10000)
//                .build()
//            jobScheduler.schedule(jobinfo)
            val serviceIntent = Intent(context, MyService::class.java).apply {
                action = MyService.ACTION_START
            }
            ContextCompat.startForegroundService(context!!, serviceIntent)
        }
//        if(intent.action==BackupManager.ACTION_RESTORE_COMPLETE)
    }

}