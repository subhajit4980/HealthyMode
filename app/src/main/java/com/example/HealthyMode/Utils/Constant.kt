package com.example.HealthyMode.Utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object Constant {
    val SanitizerNotificationId: Int = 11
    val sanitizerchannelid = "Sanitize"
    val Mealchannelid = "Meal"
    val targetStep = arrayOf("1000", "2000", "4000", "6000", "8000", "10000")
    val calorieburn =
        arrayOf(
            "10","20","30","40","50","60","70","80","90","100","110","120","130","140","150","160","170","180","190","200","210","220","230","240","250","260","270","280","290","300","310","320","330","340","350","360","370","380","390","400","410","420","430","440","450","460","470","480","490","500","510","520","530","540","550","560","570","580","590","600","610","620","630","640","650","660","670","680","690","700","710","720","730","740","750","760","770","780","790","800","810","820","830","840","850","860","870","880","890","900","910","920","930","940","950","960","970","980","990","1000","1010","1020","1030","1040","1050","1060","1070","1080","1090","1100","1110","1120","1130","1140","1150","1160","1170","1180","1190","1200","1210","1220","1230","1240","1250","1260","1270","1280","1290","1300","1310","1320","1330","1340","1350","1360","1370","1380","1390","1400","1410","1420","1430","1440","1450","1460","1470","1480","1490","1500"
    )
    val calorieTarget = arrayOf(
        "500",
        "600",
        "700",
        "800",
        "900",
        "1000",
        "1100",
        "1200",
        "1300",
        "1400",
        "1500",
        "1600",
        "1700",
        "1800",
        "1900",
        "2000",
        "2100",
        "2200",
        "2300",
        "2400",
        "2500",
    )
    val inchs = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    val ft = arrayOf("3", "4", "5", "6", "7", "8")
    fun loadData(context: Context, pre_nmae: String, key: String, default: String): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val saveNumber: String? = sharedPreferences.getString(key, default)
        Log.d("mainActivity", "$saveNumber")
        return saveNumber
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savedata(context: Context, pre_nmae: String, key: String, value: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun isInternetOn(context: Context): Boolean {
        val conncectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netinfo = conncectivityManager.activeNetworkInfo
        return netinfo != null && netinfo.isConnected
    }

    fun Lineplot(lineChart: LineChart, entries: ArrayList<Entry>, key: Boolean) {
        lineChart.description.isEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)
        lineChart.axisLeft.setDrawGridLinesBehindData(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLinesBehindData(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.axisLeft.setDrawAxisLine(false)
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.animateX(1000, Easing.EaseInExpo)
        val Yaxis = lineChart.axisLeft
        Yaxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        Yaxis.axisMinimum = 0f
        Yaxis.setDrawGridLines(false)
        val dataSet = LineDataSet(entries, "Steps")
        dataSet.setDrawValues(false)
        dataSet.formLineWidth = 1.0f
        dataSet.setCircleColors(Color.BLACK)
        dataSet.circleRadius = 4f

//                try {
//                    lineChart.renderer = WaveLineChartRenderer(
//                        lineChart,
//                        lineChart.animator,
//                        lineChart.viewPortHandler
//                    )
//                } catch (_: Exception) {
//
//                }
        dataSet.color = Color.BLACK
        dataSet.setDrawFilled(true)

        dataSet.fillFormatter = object : IFillFormatter {
            override fun getFillLinePosition(
                dataSet: ILineDataSet?, dataProvider: LineDataProvider?
            ): Float {
                return lineChart.axisLeft.axisMinimum
            }

        }
        lineChart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
        val lineData = LineData(dataSet)
        lineData.setValueTextSize(12f)
        if (key) {
            lineData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            })
            dataSet.fillDrawable = getGradientFill()
        } else {
            lineData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return String.format("%.1f", value)
                }
            })
            dataSet.lineWidth = 2.5f
            dataSet.fillDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.parseColor("#EDEDED"), Color.TRANSPARENT)
            )
        }
        val Xaxis = lineChart.xAxis
        lineChart.xAxis.isGranularityEnabled = true
        Xaxis.granularity = 1f
        lineChart.data = lineData
        lineChart.invalidate()
    }

    @SuppressLint("SuspiciousIndentation")
    fun getGradientFill(): Drawable {
        val colors = intArrayOf(Color.parseColor("#ffbd2e"), Color.TRANSPARENT)
        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
    }

    interface OnTimeSelectedListener {
        fun onTimeSelected(timeInMillis: Long, Time: String)
    }

    fun showTimePicker(v: EditText, context: Context, listener: OnTimeSelectedListener) {
        val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val formattedTime: String = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} AM"
                    } else {
                        "${hourOfDay + 12}:${minute} AM"
                    }
                }

                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} PM"
                    } else {
                        "${hourOfDay - 12}:${minute} PM"
                    }
                }

                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} PM"
                    } else {
                        "${hourOfDay}:${minute} PM"
                    }
                }

                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} AM"
                    } else {
                        "${hourOfDay}:${minute} AM"
                    }
                }
            }
            v.setText(formattedTime)
            listener.onTimeSelected((hourOfDay * 3600000 + minute * 60000).toLong(), formattedTime)
        }
        val calendar = Calendar.getInstance()
        // get the current hour and minute
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        // create a time picker dialog with the current time as the default value
        val timePickerDialog = TimePickerDialog(context, timePicker, hour, minute, false)
        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun clickDataPicker(v: EditText, context: Context, listener: OnTimeSelectedListener) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            context, { view, year, monthOfYear, dayOfMonth ->
                v.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                val date = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                val dateTime = date.atStartOfDay()
                val milliseconds =
                    dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                listener.onTimeSelected(
                    milliseconds.toLong(), "$dayOfMonth/${monthOfYear + 1}/$year"
                )
            }, year, month, day
        )
        dpd.show()
    }

    fun currentDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    val breakfasttitle = "Good Morning 🏕 🌄! It's Time for Breakfast"
    val breakfastmessage =
        "Start your day off right with a nutritious breakfast.\n Don't skip the most important meal of the day!"
    val morningsnacktitle = "Snack Time 🥜🍎🍍! Grab a Healthy Bite"
    val moriningsnackmessage =
        "Boost your energy and keep hunger at bay with a healthy morning snack.\n Try a piece of fruit or a handful of nuts."
    val lunchtitle = "Lunch Break 🍱 🍚! Fuel Your Body"
    val lunchmessage =
        "Take a break from your busy day and refuel your body with a nutritious lunch.\n Choose whole grains, lean protein, and plenty of veggies."
    val eveningsnacktitle = "Time for a Snack 🍿🍿! Keep it Light"
    val eveningmessage =
        "Satisfy your hunger without overdoing it with a light evening snack.\n Try some air-popped popcorn or a small serving of Greek yogurt."
    val dinnertitle = "Dinner Time 🍽 😴! Enjoy a Delicious Meal"
    val dinnermessage =
        "Sit down, relax, and savor a delicious dinner.\n Choose a balanced meal with plenty of veggies, whole grains, and a lean protein source."

    fun isServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)
        for (i in services.indices) {
            if (serviceClass.name == services[i].service.className) {
                return true
            }
        }
        return false
    }
    fun convertMillisecondsToDate(milliseconds: Long, dateFormatPattern: String): String {
        val dateFormat = SimpleDateFormat(dateFormatPattern)
        val date = Date(milliseconds)
        return dateFormat.format(date)
    }
}