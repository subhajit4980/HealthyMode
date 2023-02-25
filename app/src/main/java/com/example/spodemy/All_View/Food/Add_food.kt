package com.example.spodemy.All_View.Food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.spodemy.R
import kotlinx.android.synthetic.main.activity_add_food.*

class Add_food : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val break_f:ImageView=findViewById(R.id.bf)
        break_f.setOnClickListener {
            startSearchFrag(Search_meal())
        }
        ms.setOnClickListener {
            startSearchFrag(Search_meal())
        }
        lunch.setOnClickListener {
            startSearchFrag(Search_meal())
        }
        ev_sn.setOnClickListener {
            startSearchFrag(Search_meal())
        }
        din.setOnClickListener {
            startSearchFrag(Search_meal())
        }



    }
    fun startSearchFrag(frag:Fragment)
    {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content_f,frag)
            commit()
        }
    }
}