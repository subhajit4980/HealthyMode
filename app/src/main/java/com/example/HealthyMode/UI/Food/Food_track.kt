package com.example.HealthyMode.UI.Food

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.HealthyMode.R
import com.example.HealthyMode.UI.Food.ViewModel.Food_ViewModel
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.FoodTrckBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class Food_track : AppCompatActivity() {
    private lateinit var binding:FoodTrckBinding
    lateinit var pieChart: PieChart
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=FoodTrckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val ViewModel = ViewModelProvider(this)[Food_ViewModel::class.java]
        dialog= Dialog(this)
        ViewModel.nutrients.observe(this) {
            pieChart = binding.pieChart
            // on below line we are setting user percent value,
            // setting description as enabled and offset for pie chart
            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            pieChart.setDragDecelerationFrictionCoef(0.95f)

            // on below line we are setting hole
            // and hole color for pie chart
            pieChart.setDrawHoleEnabled(true)
            pieChart.setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            // on  below line we are setting hole radius
            pieChart.setHoleRadius(58f)
            pieChart.setTransparentCircleRadius(61f)

            // on below line we are setting center text
            pieChart.setDrawCenterText(true)

            // on below line we are setting
            // rotation for our pie chart
            pieChart.setRotationAngle(0f)

            // enable rotation of the pieChart by touch
            pieChart.setRotationEnabled(true)
            pieChart.setHighlightPerTapEnabled(true)

            // on below line we are setting animation for our pie chart
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            pieChart.legend.isEnabled = false
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)
            // on below line we are creating array list and
            // adding data to it to display in pie chart
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(it[0]))
            entries.add(PieEntry(it[1]))
            entries.add(PieEntry(it[2]))
            entries.add(PieEntry(it[3]))
            // on below line we are setting pie data set
            val dataSet = PieDataSet(entries, "Mobile OS")

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // add a lot of colors to list
            val colors: ArrayList<Int> = ArrayList()
            colors.add(resources.getColor(R.color.blue))
            colors.add(resources.getColor(R.color.purple))
            colors.add(resources.getColor(R.color.GREEN))
            colors.add(resources.getColor(R.color.dark_orange))

            // on below line we are setting colors.
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            pieChart.setData(data)

            // undo all highlights
            pieChart.highlightValues(null)

            // loading chart
            pieChart.invalidate()
            binding.apply {
                progress.visibility=View.GONE
                main.visibility=View.VISIBLE
                carbs.text=it[0].toString()
                sugar.text=it[1].toString()
                pro.text=it[2].toString()
                fat.text=it[3].toString()
                dialog.setContentView(R.layout.pop_weight)
                val calories_target: NumberPicker = dialog.findViewById(R.id.loss)
                val add: AppCompatButton = dialog.findViewById(R.id.add)
                calories_target.minValue = 0
                calories_target.maxValue = 20
                calories_target.wrapSelectorWheel = true
                calories_target.displayedValues = Constant.calorieTarget
                val save = Constant.loadData(this@Food_track, "calorie", "target", "500").toString()
                targetCal.text=save.toString()
                target.setOnClickListener {
                    dialog.show()
                    calories_target.value = Constant.calorieTarget.indexOf(save)
                }
                add.setOnClickListener {
                    Constant.savedata(
                        this@Food_track,
                        "calorie",
                        "target",
                        Constant.calorieTarget[calories_target.value]
                    )
                    targetCal.text= Constant.calorieTarget[calories_target.value]
                    dialog.dismiss()
                }
            }
        }
        ViewModel.getNutrients()

    }
}