package com.example.spodemy.Authentication_Asset

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
//import com.example.Spodemy.R
import com.example.spodemy.R
import com.google.android.material.tabs.TabLayout

class MainAuthentication : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_authentication)
        val topAnimation: Animation? = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomanim: Animation? = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val titletxt: TextView =findViewById(R.id.title_text)
        titletxt.animation=topAnimation
        val details: ConstraintLayout =findViewById(R.id.logindetails)
        details.animation=bottomanim
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
    class ViewPagerAdapter : FragmentPagerAdapter {
        private final var fragmentList1: ArrayList<Fragment> = ArrayList()
        private final var fragmentTitleList1: ArrayList<String> = ArrayList()

        public constructor(supportFragmentManager: FragmentManager)
                : super(supportFragmentManager)

        override fun getItem(position: Int): Fragment {
            return fragmentList1.get(position)
        }
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1.get(position)
        }
        override fun getCount(): Int {
            return fragmentList1.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }


    }
}