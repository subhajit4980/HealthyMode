package com.example.spodemy.All_View.Food

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.spodemy.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.food_chart.*

class FoodChart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_chart)
        val food:TextInputEditText=findViewById(R.id.foodname)
        fetch.setOnClickListener {
            getNutrientInformation(food.text.toString())
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getNutrientInformation(foodname:String) {
        val url = "https://api.edamam.com/api/nutrition-data?app_id=c5abc52b&app_key=33079ad0ae62991b87661eda38706f3b&nutrition-type=logging&ingr=$foodname"
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
//                val nutrients = mutableListOf<Nutrient>()
//                val totalNutrients = response.getJSONObject("totalNutrients")
//                val nutrientNames = totalNutrients.names()
//                if (nutrientNames != null) {
//                    for (i in 0 until nutrientNames.length()) {
//                        val nutrientName = nutrientNames.getString(i)
//                        val nutrientObject = totalNutrients.getJSONObject(nutrientName)
//                        val nutrientLabel = nutrientObject.getString("label")
//                        val nutrientQuantity = nutrientObject.getDouble("quantity")
//                        val nutrientUnit = nutrientObject.getString("unit")
//                        nutrients.add(Nutrient(nutrientLabel, nutrientQuantity, nutrientUnit))
//                    }
//                }
//                val nutrientText = nutrients.joinToString("\n") { "${it.label}: ${it.quantity} ${it.unit}" }
//                show.text = nutrientText
//                val calories = response.getInt("calories")
                val fat = response.getJSONObject("totalNutrients").getJSONObject("FAT").getDouble("quantity")
                val protein = response.getJSONObject("totalNutrients").getJSONObject("PROCNT").getDouble("quantity")
                val carbs = response.getJSONObject("totalNutrients").getJSONObject("CHOCDF").getDouble("quantity")
                val sugar = response.getJSONObject("totalNutrients").getJSONObject("SUGAR").getDouble("quantity")
                // Do something with the nutrient information, such as display it in a TextView
                val nutrientText = "Calories: calories\nFat: $fat\nProtein: $protein\nCarbs: $carbs\nSugar: $sugar"
                show.text = nutrientText
            },
            { error ->
                show.text = "Error retrieving nutrient information"
            })
        requestQueue.add(request)
    }
}