package com.example.spodemy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.spodemy.Home.Home_screen
import com.example.spodemy.Authentication_Asset.MainAuthentication
import com.example.spodemy.R
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
//        topAnimation= AnimationUtils.loadAnimation(this,R.anim.popup)
//        bottomAnimation= AnimationUtils.loadAnimation(this,R.anim.popup)
//        imageView =findViewById(R.id.virusc)
//        title_txt =findViewById(R.id.title_text)
//        develop_txt =findViewById(R.id.develop)
//        imageView.animation=topAnimation
//        title_txt.animation=bottomAnimation
//        develop_txt.animation=bottomAnimation
        val slide= AnimationUtils.loadAnimation(this,R.anim.popup)
        val title:TextView=findViewById(R.id.title)
//        val anima: LottieAnimationView =findViewById(R.id.animation_view)
        title.animation=slide
//        anima.animation=slide
//        develop_txt.visibility=View.GONE
        Handler().postDelayed({
            val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
            if(fAuth.currentUser !=null && fAuth.currentUser!!.isEmailVerified)
            {
                startActivity(Intent(this, Home_screen::class.java))
                finish()
            }else{
                val intent= Intent(this,MainAuthentication::class.java)
                startActivity(intent)
                finish()
            }
        },1200)

    }
}