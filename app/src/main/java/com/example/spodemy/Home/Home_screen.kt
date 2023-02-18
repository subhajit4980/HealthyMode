package com.example.spodemy.Home

import MyService
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.spodemy.Home.fragment.Home_fragment
import com.example.spodemy.Home.fragment.Plans_fragment
import com.example.spodemy.Home.fragment.profile_fragment
import com.example.spodemy.R
import kotlinx.android.synthetic.main.activity_home_screen.*

class Home_screen : AppCompatActivity() {
    private val REQUEST_PERMISSION = 10
    private val permission= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.BODY_SENSORS,android.Manifest.permission.BODY_SENSORS_BACKGROUND,Manifest.permission.ACTIVITY_RECOGNITION)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        val Home_fragment=Home_fragment()
        val plansFragment=Plans_fragment()
        val ProfileFragment=profile_fragment()
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        makeCurrentFrag(Home_fragment)
        buttonnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_t -> makeCurrentFrag(Home_fragment)
                R.id.plan -> makeCurrentFrag(plansFragment)
                R.id.profile -> makeCurrentFrag(ProfileFragment)
            }
            true
        }
        checkpermission()

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyService::class.java)
        startService(intent)
    }

    private fun checkpermission() {
        val ungrantP=permission.filter {
            ContextCompat.checkSelfPermission(this,it)!=PackageManager.PERMISSION_GRANTED
        }
        if(ungrantP.isNotEmpty())
        {
            ActivityCompat.requestPermissions(this,ungrantP.toTypedArray(),REQUEST_PERMISSION)
        }
    }

    private fun makeCurrentFrag(Fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frg,Fragment)
            commit()
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can access the location here
            } else {
                // Permission denied, you can show a message to the user
                Toast.makeText(this, "Please give the permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }
}