package com.example.HealthyMode.UI.Task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Scroller
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.HealthyMode.Application.HMApplicaton
import com.example.HealthyMode.R
import com.example.HealthyMode.TodoDatabase.Todo
import com.example.HealthyMode.UI.Task.ViewModel.TodoModel
import com.example.HealthyMode.UI.Task.ViewModel.ViewModelFactory
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.Utils.Constant.showTimePicker
import com.example.HealthyMode.databinding.ActivityAddTaskBinding
import java.util.*

class Add_Task : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityAddTaskBinding
    private val TodoViewModel: TodoModel by viewModels{
        ViewModelFactory((this.application as HMApplicaton).repository)
    }


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
//        binding.description.setMovementMethod(ScrollingMovementMethod());
    }
    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.StartDate->{
                clickDataPicker(binding.StartDate)
                return
            }
            R.id.EndDate->{
                clickDataPicker(binding.EndDate)
                return
            }
            R.id.EndTime->{
                showTimePicker(binding.EndTime,this,object : Constant.OnTimeSelectedListener {
                    override fun onTimeSelected(timeInMillis: Long,Time:String) {}
                })
            }
            R.id.StartTime->{
                showTimePicker(binding.StartTime,this,object : Constant.OnTimeSelectedListener {
                    override fun onTimeSelected(timeInMillis: Long,Time:String) {}
                })
                return
            }
            R.id.reminder->{
                binding.reminder.setText("30 min")
            }
            R.id.addplan->{
                val plan=binding.description.text.toString().trim{it<=' '}
                val startdate=binding.StartDate.text.toString().trim{it<=' '}
                val starttime=binding.StartTime.text.toString().trim{it<=' '}
                val enddate=binding.EndDate.text.toString().trim{it<=' '}
                val endtime=binding.EndTime.text.toString().trim{it<=' '}
                val rem=binding.reminder.text.toString().trim{it<=' '}
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
                            0,
                            plan,
                            startdate,
                            starttime,
                            enddate,
                            endtime,
                            rem
                        )
                        TodoViewModel.insertTodo(plans)
                        Toast.makeText(this, "Plan Set Successfully", Toast.LENGTH_SHORT).show()
                        binding.description.setText("")
                        binding.StartTime.setText("")
                        binding.StartDate.setText("")
                        binding.EndTime.setText("")
                        binding.EndDate.setText("")
                        binding.reminder.setText("")
                    }
                }
//               nav.visibility = View.VISIBLE
            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun clickDataPicker(v: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                v.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            },
            year,
            month,
            day
        )
//        dpd.datePicker.maxDate = Date().time + (86400000)
        dpd.show()
    }

}