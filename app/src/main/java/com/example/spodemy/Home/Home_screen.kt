package com.example.spodemy.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.spodemy.Home.fragment.Home_fragment
import com.example.spodemy.Home.fragment.Plans_fragment
import com.example.spodemy.Home.fragment.profile_fragment
import com.example.spodemy.R
import kotlinx.android.synthetic.main.activity_home_screen.*

class Home_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val Home_fragment=Home_fragment()
        val plansFragment=Plans_fragment()
        val ProfileFragment=profile_fragment()

        makeCurrentFrag(Home_fragment)
        buttonnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_t -> makeCurrentFrag(Home_fragment)
                R.id.plan -> makeCurrentFrag(plansFragment)
                R.id.profile -> makeCurrentFrag(ProfileFragment)
            }
            true
        }
    }
    private fun makeCurrentFrag(Fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frg,Fragment)
            commit()
        }
    }
}