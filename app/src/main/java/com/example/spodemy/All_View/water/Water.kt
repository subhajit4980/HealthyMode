
package com.example.spodemy.All_View.water

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.Adapter.WAdapter
import com.example.spodemy.data.water
import com.example.spodemy.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_water.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

class Water : AppCompatActivity() {
    private lateinit var w_RecyclerView: RecyclerView
    lateinit var adapter: WAdapter
    private var chyear:String?=null
    private var chmonth:String?=null
    @RequiresApi(Build.VERSION_CODES.O)
    private val y=LocalDate.now().year.toString()
//    private val arr_year= setOf<String>()
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        water_data()
        plotchart()
        getYear()
        getMonth()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMonth() {
        val month_arr = mutableSetOf<String>()
        val m = LocalDate.now().month.toString()
        val set_years = chyear ?: y
        userDitails.collection("water track").get().addOnSuccessListener { result ->
            for (document in result) {
                val date = document.get("Date").toString()
                val year = date.substring(0, 4)
                if (year == set_years) {
                    val months = date.substring(5, 7)
                    val m_name = Month.of(months.toInt())
                        .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                        .toUpperCase().toString()
                    month_arr.add(m_name)
                }
            }
        val mo = month_arr.toTypedArray()
        val MarrayAdapter = ArrayAdapter(this, R.layout.dropdown, mo)
        val select_month = findViewById<AutoCompleteTextView>(R.id.month)
        select_month.setAdapter(MarrayAdapter)
            if(set_years==y) {
                select_month.setText(m, false)
                chmonth=m
                plotchart()
            }else{
                select_month.setText("select month", false)
            }
        select_month.setOnItemClickListener { adapterView, view, i, l ->
            chmonth = adapterView.getItemAtPosition(i).toString()
            plotchart()
        }
    }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getYear()
    {
        val arr_year= mutableSetOf<String>()

//        thread(start = true)
//        {
        userDitails.collection("water track").get().addOnSuccessListener { result ->
            for (document in result) {
                val date = document.get("Date").toString()
                val year = date.substring(0, 4)
                arr_year.add(year)
            }
            val yr = arr_year.toTypedArray()
            val arrayAdapter = ArrayAdapter(this, R.layout.dropdown, yr)
            val select_year = findViewById<AutoCompleteTextView>(R.id.year)
            select_year.setAdapter(arrayAdapter)
            select_year.setText(y, false)
            select_year.setOnItemClickListener { adapterView, view, i, l ->
                chyear = adapterView.getItemAtPosition(i).toString()
                getMonth()

            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun plotchart() {
                val year = chyear ?: LocalDate.now().year.toString()
                val month = chmonth ?: LocalDate.now().month.toString()
            userDitails.collection("water track").get().addOnSuccessListener { result ->
                val entries = ArrayList<BarEntry>()
                for (document in result) {
                    val date = document.get("Date")
                    val count = document.get("glass")?.toString()
                    val gety = date.toString().substring(0, 4)
                    val getm = date.toString().substring(5, 7);
                    val day=date.toString().substring(8,10)
                    val m_name = Month.of(getm.toInt())
                        .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                        .toUpperCase().toString()
                    if (count != null && gety == year && m_name == month) {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        entries.add(
                            BarEntry(
                                day.toFloat(),
                                count.toFloat()
                            )
                        )
                    }
                }
                val lineChart:BarChart=findViewById(R.id.linechart)
                linechart.description.isEnabled=false
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
                lineChart.setTouchEnabled(true)
                lineChart.isDragEnabled=true
                lineChart.setScaleEnabled(false)
                lineChart.animateXY(1000, 1000,Easing.EaseInExpo)
                val Yaxis=lineChart.axisLeft
                Yaxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                Yaxis.axisMinimum=0f
//                Yaxis.valueFormatter=object :ValueFormatter(){
//                    override fun getFormattedValue(value: Float): String {
//                        return value.toInt().toString()
//                    }
//                }
//
//                lineChart.xAxis.valueFormatter=object :ValueFormatter(){
//                    override fun getFormattedValue(value: Float): String {
//                        return value.toInt().toString()
//                    }
//                }

//        Yaxis.axisMaximum=lineChart.axisLeft.axisMaximum
//        lineChart.axisRight.isEnabled=false
                Yaxis.setDrawGridLines(false)
//        lineChart.setPinchZoom(true)
                val dataSet = BarDataSet(entries, "Water intake")
//                dataSet.barWidth=0.5f
                dataSet.setColor(Color.BLUE)
                dataSet.formLineWidth=1.0f
                val lineData = BarData(dataSet)
                lineData.barWidth=.5f
                lineData.setValueTextSize(12f)
                lineData.setValueFormatter(object :ValueFormatter(){
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                })
                var daysofmonth=ArrayList<String>()
                if(month==LocalDate.now().month.toString() && year==LocalDate.now().year.toString())
                {
                    for (i in 1..LocalDate.now().dayOfMonth.toInt())
                    {
                        daysofmonth.add(i.toString())
                    }
                }else {
                    val my = year.toInt()
                    if (month == "JANUARY" || month == "MARCH" || month == "MAY" || month == "JULY" || month == "AUGUST" || month == "OCTOBER" || month == "DECEMBER") {
                        daysofmonth.addAll(
                            listOf(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8",
                                "9",
                                "10",
                                "11",
                                "12",
                                "13",
                                "14",
                                "15",
                                "16",
                                "17",
                                "18",
                                "19",
                                "20",
                                "21",
                                "22",
                                "23",
                                "24",
                                "25",
                                "26",
                                "27",
                                "28",
                                "29",
                                "30",
                                "31"
                            )
                        )
                    } else if (month == "FEBRUARY" && my % 4 == 0 && (my % 100 != 0 || my % 400 == 0)) {
                        daysofmonth.addAll(
                            listOf(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8",
                                "9",
                                "10",
                                "11",
                                "12",
                                "13",
                                "14",
                                "15",
                                "16",
                                "17",
                                "18",
                                "19",
                                "20",
                                "21",
                                "22",
                                "23",
                                "24",
                                "25",
                                "26",
                                "27",
                                "28",
                                "29"
                            )
                        )

                    } else if (month == "FEBRUARY") {
                        daysofmonth.addAll(
                            listOf(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8",
                                "9",
                                "10",
                                "11",
                                "12",
                                "13",
                                "14",
                                "15",
                                "16",
                                "17",
                                "18",
                                "19",
                                "20",
                                "21",
                                "22",
                                "23",
                                "24",
                                "25",
                                "26",
                                "27",
                                "28",
                                "29"
                            )
                        )

                    } else {
                        daysofmonth.addAll(
                            listOf(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8",
                                "9",
                                "10",
                                "11",
                                "12",
                                "13",
                                "14",
                                "15",
                                "16",
                                "17",
                                "18",
                                "19",
                                "20",
                                "21",
                                "22",
                                "23",
                                "24",
                                "25",
                                "26",
                                "27",
                                "28",
                                "29",
                                "30"
                            )
                        )
                    }
                }
                val arr=arrayOf(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31"
                )
                val formatter=IndexAxisValueFormatter(arr)
//                lineChart.xAxis.valueFormatter=formatter
                lineChart.data = lineData
                val Xaxis=lineChart.xAxis
                Xaxis.labelCount=entries.size
                lineChart.xAxis.isGranularityEnabled=true
                Xaxis.granularity=1f
//                Xaxis.axisMinimum=0f
//                Xaxis.axisMaximum=20f
//                lineChart.isScaleXEnabled=true
                lineChart.invalidate()
                progress.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
//        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun water_data() {
        val ref=userDitails.collection("water track").orderBy("glass",Query.Direction.DESCENDING)
        val query=ref
        val recyclerOptions= FirestoreRecyclerOptions.Builder<water>().setQuery(query,water::class.java).build()
        adapter= WAdapter(recyclerOptions)
        w_RecyclerView=findViewById(R.id.rc)
        w_RecyclerView.layoutManager=LinearLayoutManager(this)
        w_RecyclerView.adapter=adapter
        adapter.startListening()
    }
}