package com.example.spodemy.All_View

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.spodemy.ChartRender.WaveLineChartRenderer
import com.example.spodemy.R
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.steps_track.*
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

class Steps_Track : AppCompatActivity() {
    private var chyear:String?=null
    private var chmonth:String?=null
    @RequiresApi(Build.VERSION_CODES.O)
    private val y= LocalDate.now().year.toString()
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.steps_track)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        plotchart()
        getYear()
        getMonth()
        settarget()
        gethighesttarget()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getYear()
    {
        val arr_year= mutableSetOf<String>()
        userDitails.collection("steps").get().addOnSuccessListener { result ->
            for (document in result) {
                val date = document.get("date").toString()
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
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMonth() {
        val month_arr = mutableSetOf<String>()
        val m = LocalDate.now().month.toString()
        val set_years = chyear ?: y
        userDitails.collection("steps").get().addOnSuccessListener { result ->
            for (document in result) {
                val date = document.get("date").toString()
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
                select_month.setText("Select month", false)
            }
            select_month.setOnItemClickListener { adapterView, view, i, l ->
                chmonth = adapterView.getItemAtPosition(i).toString()
                plotchart()
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun plotchart() {
        val year = chyear ?: LocalDate.now().year.toString()
        val month = chmonth ?: LocalDate.now().month.toString()
        userDitails.collection("steps").get().addOnSuccessListener { result ->
            val entries = ArrayList<Entry>()
            for (document in result) {
                val date = document.get("date")
                val count = document.get("steps")?.toString()
                val gety = date.toString().substring(0, 4)
                val getm = date.toString().substring(5, 7);
                val day=date.toString().substring(8,10)
                val m_name = Month.of(getm.toInt())
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                    .toUpperCase().toString()
                if (count != null && gety == year && m_name == month) {
//                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    entries.add(
                        Entry(
                            day.toFloat(),
                            count.toFloat()
                        )
                    )
                }
            }
            val lineChart: LineChart =findViewById(R.id.stepchart)
            lineChart.description.isEnabled=false
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
            lineChart.setTouchEnabled(false)
            lineChart.isDragEnabled=true
            lineChart.setScaleEnabled(false)
//            lineChart.xAxis.spaceMin=3f
//            lineChart.xAxis.axisLineColor=Color.BLACK
            lineChart.animateXY(1000, 1000, Easing.EaseInExpo)
            val Yaxis=lineChart.axisLeft
            Yaxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            Yaxis.axisMinimum=0f
            Yaxis.valueFormatter=object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }

            lineChart.xAxis.valueFormatter=object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
            Yaxis.setDrawGridLines(false)
            val dataSet = LineDataSet(entries, "Steps")
            dataSet.formLineWidth=1.0f

                try {
                    lineChart.renderer = WaveLineChartRenderer(
                        lineChart,
                        lineChart.animator,
                        lineChart.viewPortHandler
                    )
                } catch (_: Exception) {

                }
                dataSet.color = Color.BLACK
                dataSet.setDrawFilled(true)
                dataSet.fillDrawable = getGradientFill()
                dataSet.fillFormatter = object : IFillFormatter {
                    override fun getFillLinePosition(
                        dataSet: ILineDataSet?,
                        dataProvider: LineDataProvider?
                    ): Float {
                        return lineChart.axisLeft.axisMinimum
                    }

                }
            val lineData = LineData(dataSet)
            lineData.setValueTextSize(12f)
            lineData.setValueFormatter(object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            })

            val Xaxis=lineChart.xAxis
            Xaxis.labelCount=entries.size
            lineChart.xAxis.isGranularityEnabled=true
            Xaxis.granularity=1f
            lineChart.data = lineData
            lineChart.invalidate()
        }
//        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun getGradientFill(): Drawable {
        val colors= intArrayOf(Color.parseColor("#ffbd2e"),Color.TRANSPARENT)
        return  GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors)
    }
    @SuppressLint("SetTextI18n")
    fun settarget()
    {
            val sharedPreferences: SharedPreferences =
                    this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val data=sharedPreferences.getString("target","1000")
            val targets= arrayOf("1000","2000","4000","6000","8000","10000")
            val arr_adapter=ArrayAdapter(this, R.layout.dropdown,targets)
            val target:AutoCompleteTextView=findViewById(R.id.target)
            target.setAdapter(arr_adapter)
            target.setText(data,false)
            target.setOnItemClickListener { adapterView, view, i, l ->
                val get_target = adapterView.getItemAtPosition(i).toString()
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("target",get_target)
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                editor.apply()
            }
    }
    fun gethighesttarget() {
        userDitails.collection("steps").orderBy("steps",
            Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            if(!result.isEmpty)
            {
                val steps=result.documents[0].get("steps")
                val date_=result.documents[0].get("date")
                step.text=steps.toString()
                date.text=date_.toString()
                progress.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
        }
    }
}