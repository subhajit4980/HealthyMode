package com.example.spodemy.All_View.Food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spodemy.Adapter.FoodNameAdapter
import com.example.spodemy.R
import com.example.spodemy.Utils.Common
import com.example.spodemy.data.Nutrient
import com.example.spodemy.databinding.ActivityAddFoodBinding
import kotlinx.android.synthetic.main.activity_add_food.*

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
        ms.setOnClickListener {
            mBundle.putString("key1","morn_snack")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()

        }
        lunch.setOnClickListener {
            mBundle.putString("key1","lunch")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
        }
        ev_sn.setOnClickListener {
            mBundle.putString("key1","evening")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
        }
        din.setOnClickListener {
            mBundle.putString("key1","dinner")
            mFragment.arguments = mBundle
            mFragmentTransaction.add(R.id.content_f, mFragment).commit()
        }
        val b_list=Common.breakfast_list
        val m_list=Common.mornsnack_list
        val lunch_list=Common.lunch_list
        val evening_list=Common.evesnack_list
        val dinner_list=Common.dinner_list
        rec_view(binding.bT,binding.breakfastRv1,b_list)
        rec_view(binding.Mt,binding.msRv,m_list)
        rec_view(binding.lun,binding.lunchRv,lunch_list)
        rec_view(binding.evSnack,binding.evRv,evening_list)
        rec_view(binding.dinT,binding.dinRv,dinner_list)
        titl.setOnClickListener {
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
}