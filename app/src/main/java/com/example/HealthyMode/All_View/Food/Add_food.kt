package com.example.HealthyMode.All_View.Food

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HealthyMode.Adapter.FoodNameAdapter
import com.example.HealthyMode.R
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
        val break_f:ImageView=findViewById(R.id.bf)
        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = Search_meal()
        val mBundle = Bundle()
        break_f.setOnClickListener {
            mBundle.putString("key1","breakfast")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
        }
        binding.ms.setOnClickListener {
            mBundle.putString("key1","morn_snack")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()

        }
        binding.lunch.setOnClickListener {
            mBundle.putString("key1","lunch")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
        }
        binding.evSn.setOnClickListener {
            mBundle.putString("key1","evening")
            mFragment.arguments = mBundle
//            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.frg,Fragment)
//                commit()
//            }
            mFragmentTransaction.apply {
                replace(R.id.content_f, mFragment)
                commit()
            }
        }
            binding.din.setOnClickListener {
            mBundle.putString("key1","dinner")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
        }
        val b_list=Constant.breakfast_list
        val m_list=Constant.mornsnack_list
        val lunch_list=Constant.lunch_list
        val evening_list=Constant.evesnack_list
        val dinner_list=Constant.dinner_list
        rec_view(binding.bT,binding.breakfastRv1,b_list)
        rec_view(binding.Mt,binding.msRv,m_list)
        rec_view(binding.lun,binding.lunchRv,lunch_list)
        rec_view(binding.evSnack,binding.evRv,evening_list)
        rec_view(binding.dinT,binding.dinRv,dinner_list)
            binding.titl.setOnClickListener {
            Toast.makeText(this, "$b_list", Toast.LENGTH_SHORT).show()
        }
    }
    fun rec_view(message:TextView,rc:RecyclerView,list:ArrayList<Nutrient>) {
        if(list.size!=0)
        {
            message.visibility=View.GONE
            rc.visibility=View.VISIBLE
            rc.layoutManager= LinearLayoutManager(this)
            rc.adapter=FoodNameAdapter(list)
            FoodNameAdapter(list).attachswipetoDelete(rc)
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