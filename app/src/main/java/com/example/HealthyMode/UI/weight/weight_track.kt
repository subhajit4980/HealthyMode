package com.example.HealthyMode.UI.weight

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.HealthyMode.Adapter.Weight
import com.example.HealthyMode.UI.weight.ViewModel.MyViewModel
import com.example.HealthyMode.R
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.Utils.UIstate
import com.example.HealthyMode.data_Model.weight
import com.example.HealthyMode.databinding.ActivityWeightTrackBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class weight_track : AppCompatActivity() {
    private lateinit var binding: ActivityWeightTrackBinding
    private lateinit var dialog: Dialog
    private var curr_weight: Float = 0.0F
    private lateinit var viewModel: MyViewModel
    private var fix_target: Float = 0.0F
    var userDitails: DocumentReference = Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid.toString()
    )

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        dialog = Dialog(this)
        binding.wloss.text = Constant.loadData(this, "weight", "loss", "0").toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            callfun()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callfun() {
        Getlatestweight()
        addTarget()
        plotchart()
        controlweight()
        timeLine()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTarget() {
        dialog.setContentView(R.layout.pop_weight)
        val lweight: NumberPicker = dialog.findViewById(R.id.loss)
        val add: AppCompatButton = dialog.findViewById(R.id.add)
        lweight.minValue = 0
        lweight.maxValue = 50
        binding.wedit.setOnClickListener {
            dialog.show()
            val save_loss = Constant.loadData(this, "weight", "loss", "0").toString()
            lweight.value = save_loss.toInt()
        }
        add.setOnClickListener {
            val lossweight = lweight.value.toString().toFloat()
            if ((curr_weight - lossweight) >= 45 && lossweight <= curr_weight) {
                Constant.savedata(this, "weight", "loss", lweight.value.toString())
                binding.wloss.text = lweight.value.toString()
                fix_target = (curr_weight - lossweight)
                Constant.savedata(this, "weight", "fixed", fix_target.toString())
                plotchart()
            } else {
                Toast.makeText(
                    this,
                    "Target weight should be greater than 45Kg",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()

        }
    }

    private fun customizeChart() {
        val lineChart = binding.weightchart
        val Xaxis = lineChart.xAxis
        Xaxis.setDrawAxisLine(false)
        Xaxis.setDrawLabels(false)
//            xAxis in bottom
        Xaxis.position = XAxis.XAxisPosition.BOTTOM
        val margin = resources.getDimensionPixelSize(R.dimen.chart_margin_bottom)
        lineChart.setExtraOffsets(0f, margin.toFloat(), 0f, margin.toFloat())
        val yAxis = lineChart.axisLeft
//            set limitline
        val weight = Constant.loadData(this, "weight", "fixed", "$curr_weight")!!.toFloat()
        val limit = LimitLine(weight, "Target ${weight}Kg")
        yAxis.removeAllLimitLines()
        yAxis.addLimitLine(limit)
        limit.lineColor = android.graphics.Color.RED
        limit.enableDashedLine(5f, 5f, 0f)
        limit.lineWidth = 3f
        limit.textSize = 15f
        limit.textColor = Color.BLUE
        limit.labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
    }

    @SuppressLint("SuspiciousIndentation", "RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    fun plotchart() {
        val lineChart: LineChart = findViewById(R.id.weightchart)
        viewModel.getWeight()
        viewModel.data.observe(this) { state ->
            when (state) {
                is UIstate.Loading -> {
                }
                is UIstate.Failure -> {
                    Toast.makeText(this, state.error.toString(), Toast.LENGTH_SHORT).show()
                }
                is UIstate.Success -> {
                    customizeChart()
                    Constant.Lineplot(lineChart, state.data, false)
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Getlatestweight() {
        userDitails.collection("Weight track").addSnapshotListener { result,e ->
            if(e!=null) return@addSnapshotListener
            if(result!=null){
                val size = result!!.documents.size
                curr_weight = result.documents[size - 1].get("weight").toString().toFloat()
                Constant.savedata(this, "weight", "curr_w", curr_weight.toString())
                binding.weightTv.text = curr_weight.toString()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun controlweight() {
        binding.apply {
            addWe.setOnClickListener {
                viewModel.changedWeight(
                    weight(
                        LocalDate.now().toString(),
                        curr_weight.plus(0.5).toString()
                    )
                )
                Getlatestweight()
                plotchart()
            }
            decWe.setOnClickListener {
                viewModel.changedWeight(
                    weight(
                        LocalDate.now().toString(),
                        curr_weight.minus(0.5).toString()
                    )
                )
                Getlatestweight()
                plotchart()
            }
        }
    }

    private fun timeLine() {
        val ref = userDitails.collection("Weight track")
        val query = ref
        val recyclerOptions =
            FirestoreRecyclerOptions.Builder<weight>().setQuery(query, weight::class.java).build()
        val adapter = Weight(recyclerOptions)
        val w_RecyclerView = binding.timelineView
        w_RecyclerView.layoutManager = LinearLayoutManager(this)
        w_RecyclerView.adapter = adapter
        adapter.startListening()
    }
}
