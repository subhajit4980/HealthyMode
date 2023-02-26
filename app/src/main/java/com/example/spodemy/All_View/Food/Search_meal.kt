package com.example.spodemy.All_View.Food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.spodemy.R
import com.example.spodemy.Utils.Common
import com.example.spodemy.data.Nutrient
import com.example.spodemy.databinding.FragmentSearchMealBinding
import kotlinx.android.synthetic.main.fragment_search_meal.*
import java.text.DecimalFormat

class Search_meal : Fragment() {
    private var unit_:String?=null
    private var fat :String?=null
    private var protein :String?=null
    private var carbs :String?=null
    private var sugar :String?=null
    private var calories :String?=null
    private val df=DecimalFormat("#.##")
    private lateinit var loading: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentSearchMealBinding.inflate(inflater,container,false)
        val measurement=resources.getStringArray(R.array.Measures)
        val arrayAdapter= ArrayAdapter(requireContext(), R.layout.dropdown,measurement)
        binding.unit.setAdapter(arrayAdapter)
        binding.unit.setOnItemClickListener { adapterView, view, i, l ->
            unit_=adapterView.getItemAtPosition(i).toString()
        }
        loading=Dialog(requireContext())
        loading.setContentView(R.layout.loading)
        loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val list=arguments?.getString("key1")
        binding.backward.setOnClickListener {
            startActivity(Intent(requireActivity(),Add_food::class.java))
            requireActivity().finish()
        }
        binding.chnut.setOnClickListener {
            binding.nutrients.visibility=View.GONE
            binding.ER.visibility=View.GONE
            if (binding.foodName.text!!.isEmpty())
            {
                binding.foodName.error="required"
            }else if (binding.quantity.text!!.isEmpty())
            {
                binding.quantity.error="required"
            }else if (binding.unit.text!!.isEmpty())
            {
                binding.unit.error="required"
            }else{
                try{
                    loading.show()
                    getNutrientInformation(binding.quantity.text.toString(),binding.unit.text.toString(),binding.foodName.text.toString(),object :ApiCallback{
                        override fun onSuccess(
                            cal: String,
                            fat: String,
                            pro: String,
                            carbs: String,
                            sugar: String
                        ) {
                            binding.fat.text=fat.toString()
                            binding.prot.text=pro.toString()
                            binding.carbs.text=carbs.toString()
                            binding.sugar.text=sugar.toString()
                            binding.cal.text=cal.toString()
                            if(sugar!!.isNotEmpty() && carbs!!.isNotEmpty()&&carbs!!.isNotEmpty()&&fat!!.isNotEmpty())
                                loading.dismiss()
                                binding.nutrients.visibility=View.VISIBLE
                        }
                        override fun onError(error: String) {
                            binding.ER.visibility=View.VISIBLE
                            binding.ER.text = "No item founds !"
                        }
                    })
                }catch (_: RuntimeException)
                {
                    binding.ER.visibility=View.VISIBLE
                    binding.ER.text = "No item founds !"
                }
            }
        }
        binding.addFood.setOnClickListener {
            if (binding.foodName.text!!.isEmpty())
            {
                binding.foodName.error="required"
            }else if (binding.quantity.text!!.isEmpty())
            {
                binding.quantity.error="required"
            }else if (binding.unit.text!!.isEmpty())
            {
                binding.unit.error="required"
            }else{
                    loading.show()
                    getNutrientInformation(binding.quantity.text.toString(),binding.unit.text.toString(),binding.foodName.text.toString(),object :ApiCallback{
                        override fun onSuccess(
                            cal: String,
                            fat: String,
                            pro: String,
                            carbs: String,
                            sugar: String
                        ) {
                            if(sugar.isNotEmpty() && pro.isNotEmpty()&&carbs.isNotEmpty()&&fat.isNotEmpty()&&cal.isNotEmpty())
                            {
                                if (list.toString()=="breakfast")
                                {
                                    Common.breakfast_list.add(Nutrient(binding.foodName.text.toString(),cal.toString(),fat.toString(),pro.toString(),carbs.toString(),sugar.toString()))
                                }
                                if (list.toString()=="morn_snack")
                                {
                                    Common.mornsnack_list.add(Nutrient(binding.foodName.text.toString(),calories.toString(),fat.toString(),protein.toString(),carbs.toString(),sugar.toString()))
                                }
                                if (list.toString()=="lunch")
                                {
                                    Common.lunch_list.add(Nutrient(binding.foodName.text.toString(),calories.toString(),fat.toString(),protein.toString(),carbs.toString(),sugar.toString()))
                                }
                                if (list.toString()=="evening")
                                {
                                    Common.evesnack_list.add(Nutrient(binding.foodName.text.toString(),calories.toString(),fat.toString(),protein.toString(),carbs.toString(),sugar.toString()))
                                }
                                if (list.toString()=="dinner")
                                {
                                    Common.dinner_list.add(Nutrient(binding.foodName.text.toString(),calories.toString(),fat.toString(),protein.toString(),carbs.toString(),sugar.toString()))
                                }
                                loading.dismiss()
                                binding.nutrients.visibility=View.GONE
                                binding.foodName.text!!.clear()
                                binding.quantity.text!!.clear()
                                binding.unit.setText("")
                            }
                        }
                        override fun onError(error: String) {
                            loading.dismiss()
                            binding.ER.visibility=View.VISIBLE
                            binding.ER.text = "No item founds !"
                        }
                    })
            }
        }
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    private fun getNutrientInformation(quantity:String, Unit:String, foodname:String,callback: ApiCallback) {

        val url = "https://api.edamam.com/api/nutrition-data?app_id=c5abc52b&app_key=33079ad0ae62991b87661eda38706f3b&nutrition-type=logging&ingr=$quantity%20$Unit%20$foodname"
        val requestQueue = Volley.newRequestQueue(requireActivity())
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    calories =df.format(response.getJSONObject("totalNutrientsKCal").getJSONObject("ENERC_KCAL").getDouble("quantity")).toString()
                    fat = df.format(response.getJSONObject("totalNutrients").getJSONObject("FAT").getDouble("quantity")).toString()
                    protein = df.format(response.getJSONObject("totalNutrients").getJSONObject("PROCNT").getDouble("quantity")).toString()
                    carbs =df.format (response.getJSONObject("totalNutrients").getJSONObject("CHOCDF").getDouble("quantity")).toString()
                    sugar =df.format( response.getJSONObject("totalNutrients").getJSONObject("SUGAR").getDouble("quantity")).toString()
                    callback.onSuccess(calories!!, fat!!,protein!!,carbs!!,sugar!!)
                }catch (E:Exception)
                {
                    loading.dismiss()
                    ER.visibility=View.VISIBLE
                    ER.text = "No item founds !"
                }
            },
            { error ->
                ER.visibility=View.VISIBLE
                ER.text = "No item founds !"
            })
        requestQueue.add(request)
    }
    interface ApiCallback{
        fun onSuccess(cal:String,fat:String,pro:String,carbs:String,sugar:String)
        fun onError(error:String)
    }
}