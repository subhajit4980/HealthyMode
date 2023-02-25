package com.example.spodemy

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

//import java.util.*

class ResetStepCountWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val sharedPref = applicationContext.getSharedPreferences("pedometer_pref", Context.MODE_PRIVATE)
        val lastDate = sharedPref.getString("last_date", null)
        val today = Calendar.getInstance().time
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today)

        return if (lastDate != todayStr) {
            sharedPref.edit().apply {
                putInt("step_count", 0)
                putString("last_date", todayStr)
                apply()
            }
            // reset successful
            Result.success()
        } else {
            // no reset needed
            Result.failure()
        }
    }
}