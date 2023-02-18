package com.example.spodemy.Home.Food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.spodemy.R

class Add_food : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    }
}