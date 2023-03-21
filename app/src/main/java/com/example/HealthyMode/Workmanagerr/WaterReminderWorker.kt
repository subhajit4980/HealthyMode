//package com.example.HealthyMode.Workmanagerr
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import com.example.HealthyMode.Application.HMApplicaton
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class WaterReminderWorker(private val context: Context,params:WorkerParameters) :Worker(context,params){
//    @SuppressLint("LongLogTag")
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun doWork(): Result {
//        val schedule=(context as HMApplicaton).WaterReminder
////        Log.d("SUBHAJIT        :" ," schedule reminder")
////        Log.d("SUBHAJIT     inTERVAL===================>   :" ," ${Constant.loadData(context, "reminder", "interval", "1")}")
//        CoroutineScope(Dispatchers.IO).launch {
////            schedule.sheduleNotification(context)
////            delay(60000)
//        }
//        return Result.success()
//    }
//
//}