
package com.example.spodemy.Home.water

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.model.content.GradientFill
import com.example.spodemy.Adapter.WAdapter
import com.example.spodemy.ChartRender.WaveLineChartRenderer
import com.example.spodemy.data.water
import com.example.spodemy.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_water.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Water : AppCompatActivity() {
    private lateinit var w_RecyclerView: RecyclerView
    lateinit var adapter: WAdapter
    private var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(FirebaseAuth.getInstance().currentUser!!.uid.toString().toString())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        water_data()
        plotchart()
    }

    private fun plotchart() {
        val lineChart:LineChart=findViewById(R.id.linechart)
        linechart.description.isEnabled=false
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)
        lineChart.axisLeft.setDrawGridLinesBehindData(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLinesBehindData(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.setTouchEnabled(false)
        lineChart.isDragEnabled=true
        lineChart.setScaleEnabled(false)
        lineChart.animateXY(1000, 1000,Easing.EaseInExpo)
        val Yaxis=lineChart.axisLeft
        Yaxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        Yaxis.axisMinimum=0f
//        Yaxis.axisMaximum=lineChart.axisLeft.axisMaximum
//        lineChart.axisRight.isEnabled=false
        Yaxis.setDrawGridLines(false)
//        lineChart.setPinchZoom(true)
        userDitails.collection("water track").get().addOnSuccessListener{ result ->
            val entries = ArrayList<Entry>()
            for (document in result) {
                val date = document.get("Date")
                val count = document.get("glass")?.toString()
                if (count != null) {
                    val dateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    entries.add(Entry(dateFormat.parse(date as String)!!.time.toFloat(), count.toFloat()))
                }
            }

            val dataSet = LineDataSet(entries, "Water intake")
            try {
                lineChart.renderer=WaveLineChartRenderer(lineChart,lineChart.animator,lineChart.viewPortHandler)
            }catch (_:Exception)
            {

            }
            dataSet.color= Color.BLACK
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable=getGradientFill()
            dataSet.fillFormatter=object :IFillFormatter{
                override fun getFillLinePosition(
                    dataSet: ILineDataSet?,
                    dataProvider: LineDataProvider?
                ): Float {
                    return lineChart.axisLeft.axisMinimum
                }

            }
            val lineData = LineData(dataSet)

            lineChart.data = lineData
            lineChart.invalidate()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun getGradientFill(): Drawable {
    val colors= intArrayOf(Color.parseColor("#ffbd2e"),Color.TRANSPARENT)
        return  GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,colors)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun water_data() {
        val ref=userDitails.collection("water track").orderBy("Date",Query.Direction.DESCENDING)
        val query=ref
        val recyclerOptions= FirestoreRecyclerOptions.Builder<water>().setQuery(query,water::class.java).build()
        adapter= WAdapter(recyclerOptions)
        w_RecyclerView=findViewById(R.id.rc)
        w_RecyclerView.layoutManager=LinearLayoutManager(this)
        w_RecyclerView.adapter=adapter
        adapter.startListening()
    }
}