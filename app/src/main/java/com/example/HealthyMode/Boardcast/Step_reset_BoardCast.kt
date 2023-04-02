package com.example.HealthyMode.Boardcast

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant

class Step_reset_BoardCast : BroadcastReceiver() {
    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, p1: Intent?) {
        val pre_step = Constant.loadData(context!!, "step_count", "total_step", "0")!!.toInt()
        Constant.savedata(context, "step_count", "previous_step", pre_step.toString())
        val step = Constant.loadData(context, "step_count", "total_step", "0")
        val previoustotalstep =
            Constant.loadData(context, "step_count", "previous_step", "0")!!.toInt() ?: 0
        Log.d("TAG-------------------------------> ", "reset successfully")
        val c_step = Math.abs(step!!.toInt() - previoustotalstep)
        val target = Constant.loadData(context, "myPrefs", "target", "1000").toString()
        val notification = NotificationCompat.Builder(context, "Stepcount")
            .setContentTitle("Tracking steps...")
            .setContentText("Current Steps : $c_step  Target Steps:$target")
            .setSmallIcon(R.drawable.mainlogo)
            .setOngoing(true).setSilent(true)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())
//        startForeground(1, notification.build())
    }
}