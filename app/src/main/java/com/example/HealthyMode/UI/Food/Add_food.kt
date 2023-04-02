package com.example.HealthyMode.UI.Food

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.Adapter.FoodNameAdapter
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.data_Model.Nutrient
import com.example.HealthyMode.databinding.ActivityAddFoodBinding

class Add_food : AppCompatActivity() {
    private lateinit var binding: ActivityAddFoodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val intent = Intent(this,SearchMeal::class.java)
        binding.bf.setOnClickListener {
            intent.putExtra("key1","breakfast")
            startActivity(intent)
            finish()
        }
        binding.ms.setOnClickListener {
            intent.putExtra("key1","morn_snack")
            startActivity(intent)
            finish()
        }
        binding.lunch.setOnClickListener {
            intent.putExtra("key1","lunch")
            startActivity(intent)
            finish()
        }
        binding.evSn.setOnClickListener {
            intent.putExtra("key1","evening")
            startActivity(intent)
            finish()
        }
        binding.din.setOnClickListener {
            intent.putExtra("key1","dinner")
            startActivity(intent)
            finish()
        }
        Handler().post(
           object :Runnable{
               override fun run() {
                   val b_list= Constant.breakfast_list
                   val m_list= Constant.mornsnack_list
                   val lunch_list= Constant.lunch_list
                   val evening_list= Constant.evesnack_list
                   val dinner_list= Constant.dinner_list
                   rec_view(binding.bT,binding.breakfastRv1,b_list)
                   rec_view(binding.Mt,binding.msRv,m_list)
                   rec_view(binding.lun,binding.lunchRv,lunch_list)
                   rec_view(binding.evSnack,binding.evRv,evening_list)
                   rec_view(binding.dinT,binding.dinRv,dinner_list)
                   Handler().postDelayed(this, 400)
               }
           })

    }
    fun rec_view(message:TextView,rc:RecyclerView,list:ArrayList<Nutrient>) {
        if(list.size>=1)
        {
            rc.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            message.visibility=View.GONE
            rc.visibility=View.VISIBLE
            rc.layoutManager= LinearLayoutManager(this)
            rc.adapter=FoodNameAdapter(list)
            FoodNameAdapter(list).attachswipetoDelete(rc)
        }else{
            message.visibility=View.VISIBLE
            rc.visibility=View.GONE
        }
    }

//    override fun onBackPressed() {
//        val fragment =
//            this.supportFragmentManager.findFragmentById(R.id.content_f)
//        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
//            super.onBackPressed()
//        }
//    }
}