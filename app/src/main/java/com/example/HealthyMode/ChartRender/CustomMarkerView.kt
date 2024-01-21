package com.example.HealthyMode.ChartRender

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant.convertMillisecondsToDate
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context, layoutResource: Int, private val check:Long) : MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.chartContent)
    private val date: TextView = findViewById(R.id.date)

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e == null) {
            Toast.makeText(context, " null value", Toast.LENGTH_SHORT).show()
            return
        }
        tvContent.text = "${e.y}"
        if(check==0L)
            date.text=convertMillisecondsToDate(e.x.toLong() ,"dd-MM-yyyy")
        super.refreshContent(e, highlight)
    }
    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}