package com.example.HealthyMode.UI.Auth

//import com.example.Spodemy.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.HealthyMode.Adapter.ViewPagerAdapter
import com.example.HealthyMode.R
import com.example.HealthyMode.databinding.ActivityMainAuthenticationBinding
import com.google.android.material.tabs.TabLayout

class MainAuthentication : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityMainAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val topAnimation: Animation? = AnimationUtils.loadAnimation(this, R.anim.left_right)
        val bottomanim: Animation? = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        binding.titleText.animation=topAnimation
        binding.animationView2.playAnimation()
        binding.logindetails.animation=bottomanim
        val tabLayout: TabLayout =findViewById(R.id.tab_layout)
        val viewpager: ViewPager =findViewById(R.id.view_pager)
        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("signup"))
        tabLayout.tabGravity= TabLayout.GRAVITY_FILL
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)
    }
    companion object{
        private const val RC_SIGN_IN=120
    }
    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Login_fragment(), "Login")
        adapter.addFragment(signup_fragment(), "Signup")
        viewpager.setAdapter(adapter)
    }

}