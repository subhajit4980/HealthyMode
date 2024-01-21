package com.example.HealthyMode.UI.step

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.HealthyMode.ChartRender.CustomMarkerView
import com.example.HealthyMode.R
import com.example.HealthyMode.Service.MyService
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.StepsTrackBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

class StepsTrack : AppCompatActivity() {
    private var chyear:String?=null
    private var chmonth:String?=null
    @RequiresApi(Build.VERSION_CODES.O)
    private val y= LocalDate.now().year.toString()
    private lateinit var binding: StepsTrackBinding
    var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid.toString())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding=StepsTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
//        val num=Month.valueOf(month).value
//        val msec=year.toLong()*31_536_000_000L +(num.toLong()*30.44 * 24 * 60 * 60 * 1000).toLong()
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
                    entries.add(
                        Entry(
                            day.toFloat(),
                            count.toFloat()
                        )
                    )
                }
            }
            val lineChart: LineChart =findViewById(R.id.stepchart)
            val Xaxis=lineChart.xAxis
            Xaxis.labelCount=entries.size
            val margin=resources.getDimensionPixelSize(R.dimen.chart_margin_bottom)
            lineChart.setExtraOffsets(10f,margin.toFloat(),10f, margin.toFloat())
            val mv = CustomMarkerView(this,R.layout.tvcontent, 1L)
            lineChart.markerView=mv
            Constant.Lineplot(lineChart,entries,true)

        }
    }

    @SuppressLint("SetTextI18n")
    fun settarget()
    {
            val sharedPreferences: SharedPreferences =
                    this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val data=sharedPreferences.getString("target","1000")
            val targets= Constant.targetStep
            val arr_adapter=ArrayAdapter(this, R.layout.dropdown,targets)
            val target:AutoCompleteTextView=findViewById(R.id.target)
            target.setAdapter(arr_adapter)
            target.setText(data,false)
            target.setOnItemClickListener { adapterView, view, i, l ->
                val get_target = adapterView.getItemAtPosition(i).toString()
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("target",get_target)
                editor.apply()
                val intent = Intent(this, MyService::class.java).apply {
                    action = MyService.ACTION_START
                }
                startService(intent)
            }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun gethighesttarget() {
        data class Highstep(val step:Int,val date:String)
        val step_=PriorityQueue<Highstep>(compareByDescending { it.step })
        userDitails.collection("steps").orderBy("steps",
            Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            if(!result.isEmpty)
            {
                for(document in result)
                {
                    val steps=document.get("steps").toString().toInt()
                    val date_=document.get("date").toString()
                    step_.add(Highstep(steps,date_))
                }
                val high=step_.peek()
                if (high != null) {
                    binding.step.text=high.step.toString()
                }
                if (high != null) {
                    binding.date.text=high.date.toString()
                }
                binding.progress.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
            }
        }
    }
}