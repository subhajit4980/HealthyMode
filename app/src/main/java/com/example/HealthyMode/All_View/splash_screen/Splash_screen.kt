package com.example.HealthyMode.All_View.splash_screen

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.HealthyMode.All_View.Authentication_View.MainAuthentication
import com.example.HealthyMode.All_View.Home.Home_screen
import com.example.HealthyMode.R
import com.example.HealthyMode.Service.Jobservice
import com.example.HealthyMode.Service.MyService
import com.google.firebase.auth.FirebaseAuth

class Splash_screen : AppCompatActivity() {
    val SPLASH_SCREEN=5000
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var imageView: ImageView
    private lateinit var title_txt: TextView
    private lateinit var develop_txt: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val slide= AnimationUtils.loadAnimation(this, R.anim.popup)
        val title:TextView=findViewById(R.id.title)
        title.animation=slide
        Handler().postDelayed({
            val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
            if(fAuth.currentUser !=null && fAuth.currentUser!!.isEmailVerified)
            {
                startActivity(Intent(this, Home_screen::class.java))
                finish()
            }else{
                val intent= Intent(this, MainAuthentication::class.java)
                startActivity(intent)
                finish()
            }
        },1200)

    }
    @SuppressLint("LongLogTag")
    override fun onStart() {
        super.onStart()
        val jobsh = applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        var flag: Boolean = false
        for (job in jobsh.allPendingJobs) {
            if (job.id == 1) {
                flag = true
            }
        }
        if (!flag) {
            val jobinfo =
                JobInfo.Builder(1, ComponentName(this, Jobservice::class.java))
                    .setMinimumLatency(10000)
                    .build()
            jobsh.schedule(jobinfo)
        }
        val intent = Intent(this, MyService::class.java).apply {
            action = MyService.ACTION_START
        }
        startService(intent)
    }

}