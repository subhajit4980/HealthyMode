package com.example.HealthyMode.UI.Task

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Scroller
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.HealthyMode.R
import com.example.HealthyMode.TodoDatabase.Todo
import com.example.HealthyMode.UI.Task.ViewModel.TodoModel
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.Utils.Constant.clickDataPicker
import com.example.HealthyMode.Utils.Constant.showTimePicker
import com.example.HealthyMode.databinding.ActivityAddTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
@AndroidEntryPoint
class Add_Task : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityAddTaskBinding
    private var time: Long = 0
    private val ViewModel:TodoModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.StartDate.setOnClickListener(this)
        binding.StartTime.setOnClickListener(this)
        binding.EndDate.setOnClickListener(this)
        binding.EndTime.setOnClickListener(this)
        binding.addplan.setOnClickListener(this)
        binding.description.setScroller(Scroller(this))
        binding.description.isVerticalScrollBarEnabled=true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.StartDate->{
                clickDataPicker(binding.StartDate,this,object : Constant.OnTimeSelectedListener {
                    override fun onTimeSelected(timeInMillis: Long,Time:String) {
                        time+=timeInMillis
                    }
                })
            }
            R.id.EndDate->{
                clickDataPicker(binding.EndDate,this,object : Constant.OnTimeSelectedListener {
                    override fun onTimeSelected(timeInMillis: Long,Time:String) {}
                })
            }
            R.id.EndTime->{
                showTimePicker(binding.EndTime,this,object : Constant.OnTimeSelectedListener {
                    override fun onTimeSelected(timeInMillis: Long,Time:String) {}
                })
            }
            R.id.StartTime->{
                showTimePicker(binding.StartTime,this,object : Constant.OnTimeSelectedListener {
                    override fun onTimeSelected(timeInMillis: Long,Time:String) {
                        time+=timeInMillis
                    }
                })
                return
            }
            R.id.addplan->{
                val plan=binding.description.text.toString().trim{it<=' '}
                val startdate=binding.StartDate.text.toString().trim{it<=' '}
                val starttime=binding.StartTime.text.toString().trim{it<=' '}
                val enddate=binding.EndDate.text.toString().trim{it<=' '}
                val endtime=binding.EndTime.text.toString().trim{it<=' '}
                when{
                    plan.isEmpty() -> {
                        binding.description.error = "Required"
                        return
                    }
                    startdate.isEmpty() -> {
                        binding.StartDate.error = "Required"
                        return
                    }
                    starttime.isEmpty() -> {
                        binding.StartTime.error = "Required"
                        return
                    }
                    enddate.isEmpty() -> {
                        binding.EndDate.error = "Required"
                        return
                    }
                    endtime.isEmpty() -> {
                        binding.EndTime.error = "Required"
                        return
                    }
                    else -> {
                        val plans: Todo = Todo(
                            false,
                            plan,
                            startdate,
                            starttime,
                            enddate,
                            endtime,
                            "",
                            time
                        )
                        ViewModel.insertTodo(plans)
                        Toast.makeText(this, "Plan Set Successfully", Toast.LENGTH_SHORT).show()
                        binding.description.setText("")
                        binding.StartTime.setText("")
                        binding.StartDate.setText("")
                        binding.EndTime.setText("")
                        binding.EndDate.setText("")
                    }
                }
            }
        }
    }

}