package com.example.HealthyMode.UI.Food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.HealthyMode.R
import com.example.HealthyMode.UI.Food.ViewModel.Food_ViewModel
import com.example.HealthyMode.data_Model.Nutrient
import com.example.HealthyMode.databinding.SearchmealBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class SearchMeal : AppCompatActivity() {
    private var unit_: String? = null
    private var fat: String? = null
    private var protein: String? = null
    private var carbs: String? = null
    private var sugar: String? = null
    private var calories: String? = null
    private val df = DecimalFormat("#.##")
    private lateinit var loading: Dialog
    private lateinit var binding: SearchmealBinding
    private lateinit var ViewModel: Food_ViewModel
    private var userDitails: DocumentReference = Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid.toString()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchmealBinding.inflate(layoutInflater)
        ViewModel = ViewModelProvider(this)[Food_ViewModel::class.java]
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val measurement = resources.getStringArray(R.array.Measures)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown, measurement)
        binding.unit.setAdapter(arrayAdapter)
        binding.unit.setOnItemClickListener { adapterView, view, i, l ->
            unit_ = adapterView.getItemAtPosition(i).toString()
        }
        loading = Dialog(this)
        loading.setContentView(R.layout.loading)
        loading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val list = intent.getStringExtra("key1")
        binding.backward.setOnClickListener {
            startActivity(Intent(this, Add_food::class.java))
            this.finish()
        }
        binding.chnut.setOnClickListener {
            binding.nutrients.visibility = View.GONE
            binding.ER.visibility = View.GONE
            if (binding.foodName.text!!.isEmpty()) {
                binding.foodName.error = "required"
            } else if (binding.quantity.text!!.isEmpty()) {
                binding.quantity.error = "required"
            } else if (binding.unit.text!!.isEmpty()) {
                binding.unit.error = "required"
            } else {
                try {
                    loading.show()
                    getNutrientInformation(
                        binding.quantity.text.toString(),
                        binding.unit.text.toString(),
                        binding.foodName.text.toString(),
                        object : ApiCallback {
                            @SuppressLint("SuspiciousIndentation")
                            override fun onSuccess(
                                cal: String,
                                fat: String,
                                pro: String,
                                carbs: String,
                                sugar: String,
                                Unit: String,
                                quantity: String
                            ) {
                                binding.fat.text = fat.toString()
                                binding.prot.text = pro.toString()
                                binding.carbs.text = carbs.toString()
                                binding.sugar.text = sugar.toString()
                                binding.cal.text = cal.toString()
                                if (sugar!!.isNotEmpty() && carbs!!.isNotEmpty() && carbs!!.isNotEmpty() && fat!!.isNotEmpty())
                                    loading.dismiss()
                                binding.nutrients.visibility = View.VISIBLE
                            }

                            override fun onError(error: String) {
                                binding.ER.visibility = View.VISIBLE
                                binding.ER.text = "No item founds !"
                            }
                        })
                } catch (_: RuntimeException) {
                    binding.ER.visibility = View.VISIBLE
                    binding.ER.text = "No item founds !"
                }
            }
        }
        binding.addFood.setOnClickListener {
            if (binding.foodName.text!!.isEmpty()) {
                binding.foodName.error = "required"
            } else if (binding.quantity.text!!.isEmpty()) {
                binding.quantity.error = "required"
            } else if (binding.unit.text!!.isEmpty()) {
                binding.unit.error = "required"
            } else {
                loading.show()
                getNutrientInformation(
                    binding.quantity.text.toString(),
                    binding.unit.text.toString(),
                    binding.foodName.text.toString(),
                    object : ApiCallback {
                        override fun onSuccess(
                            cal: String,
                            fat: String,
                            pro: String,
                            carbs: String,
                            sugar: String,
                            Unit: String,
                            quantity: String
                        ) {
                            if (sugar.isNotEmpty() && pro.isNotEmpty() && carbs.isNotEmpty() && fat.isNotEmpty() && cal.isNotEmpty()) {
                                ViewModel.addFood(
                                    Nutrient(
                                        list.toString(),
                                        binding.foodName.text.toString(),
                                        cal,
                                        fat,
                                        pro,
                                        carbs,
                                        sugar,
                                        Unit,
                                        quantity
                                    )
                                )
                                loading.dismiss()
                                binding.nutrients.visibility = View.GONE
                                binding.foodName.text!!.clear()
                                binding.quantity.text!!.clear()
                                binding.unit.setText("")
                            }
                        }

                        override fun onError(error: String) {
                            loading.dismiss()
                            binding.ER.visibility = View.VISIBLE
                            binding.ER.text = "No item founds !"
                        }
                    })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getNutrientInformation(
        quantity: String,
        Unit: String,
        foodname: String,
        callback: ApiCallback
    ) {

        val url =
            "https://api.edamam.com/api/nutrition-data?app_id=c5abc52b&app_key=33079ad0ae62991b87661eda38706f3b&nutrition-type=logging&ingr=$quantity%20$Unit%20$foodname"
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    calories = df.format(
                        response.getJSONObject("totalNutrientsKCal").getJSONObject("ENERC_KCAL")
                            .getDouble("quantity")
                    ).toString()
                    fat = df.format(
                        response.getJSONObject("totalNutrients").getJSONObject("FAT")
                            .getDouble("quantity")
                    ).toString()
                    protein = df.format(
                        response.getJSONObject("totalNutrients").getJSONObject("PROCNT")
                            .getDouble("quantity")
                    ).toString()
                    carbs = df.format(
                        response.getJSONObject("totalNutrients").getJSONObject("CHOCDF")
                            .getDouble("quantity")
                    ).toString()
                    sugar = df.format(
                        response.getJSONObject("totalNutrients").getJSONObject("SUGAR")
                            .getDouble("quantity")
                    ).toString()
                    callback.onSuccess(
                        calories!!,
                        fat!!,
                        protein!!,
                        carbs!!,
                        sugar!!,
                        Unit,
                        quantity
                    )
                } catch (E: Exception) {
                    loading.dismiss()
                    binding.ER.visibility = View.VISIBLE
                    binding.ER.text = "No item founds !"
                }
            },
            { error ->
                binding.ER.visibility = View.VISIBLE
                binding.ER.text = "No item founds !"
            })
        requestQueue.add(request)
    }

    interface ApiCallback {
        fun onSuccess(
            cal: String,
            fat: String,
            pro: String,
            carbs: String,
            sugar: String,
            Unit: String,
            quantity: String
        )

        fun onError(error: String)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Add_food::class.java))
    }
}