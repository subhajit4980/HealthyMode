package com.example.spodemy.Utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.spodemy.data.Nutrient

object Common {
    val breakfast_list=ArrayList<Nutrient>()
    val mornsnack_list=ArrayList<Nutrient>()
    val lunch_list=ArrayList<Nutrient>()
    val evesnack_list=ArrayList<Nutrient>()
    val dinner_list=ArrayList<Nutrient>()


    val totalKcal= breakfast_list.sumOf { it.calories.toInt() }+ mornsnack_list.sumOf { it.calories.toInt() }+
            lunch_list.sumOf { it.calories.toInt() }+ evesnack_list.sumOf { it.calories.toInt() }+ dinner_list.sumOf { it.calories.toInt() }



    fun loadData(context: Context,pre_nmae:String,key:String,default:String): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val saveNumber: String? = sharedPreferences.getString(key, default)
        Log.d("mainActivity", "$saveNumber")
        return saveNumber
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun savedata(context: Context, pre_nmae:String, key:String, value:String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(pre_nmae, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
}