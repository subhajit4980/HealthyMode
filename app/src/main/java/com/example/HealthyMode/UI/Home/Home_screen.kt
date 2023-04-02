package com.example.HealthyMode.UI.Home

//import kotlinx.android.synthetic.main.activity_home_screen.*
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.backup.BackupManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.HealthyMode.Boardcast.Step_reset_BoardCast
import com.example.HealthyMode.R
import com.example.HealthyMode.Service.MyService
import com.example.HealthyMode.UI.Home_fragment.Home_fragment
import com.example.HealthyMode.UI.Home_fragment.Plans_fragment
import com.example.HealthyMode.UI.Home_fragment.Stats
import com.example.HealthyMode.UI.Home_fragment.profile_fragment
import com.example.HealthyMode.Utils.Constant
import com.example.HealthyMode.databinding.ActivityHomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.util.*

@Suppress("UNREACHABLE_CODE")
class Home_screen : AppCompatActivity() {
    @RequiresApi(VERSION_CODES.TIRAMISU)
    private var permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.POST_NOTIFICATIONS
    )
    private val REQUEST_CODE_PERMISSIONS = 11;
    public lateinit var buttonnav: BottomNavigationView
    private var userDitails: DocumentReference = Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid.toString()
    )
    private lateinit var binding: ActivityHomeScreenBinding

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonnav.visibility = View.VISIBLE
        checkpermission()
        val Home_fragment = Home_fragment()
        val plansFragment = Plans_fragment()
        val ProfileFragment = profile_fragment()
        val Weight = AddWeight()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        userDitails.collection("Weight track").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                binding.buttonnav.visibility = View.GONE
                supportFragmentManager.beginTransaction().apply {
                    add(R.id.frg, Weight)
                    commit()
                }
            } else {
                makeCurrentFrag(Home_fragment)
                Getlatestweight()
                KeyboardVisibilityEvent.setEventListener(
                    this
                ) { isOpen ->
                    if (isOpen) {
                        binding.buttonnav.visibility = View.INVISIBLE
                    } else {
                        binding.buttonnav.visibility = View.VISIBLE
                    }
                }
                binding.buttonnav.visibility = View.VISIBLE
                buttonnav=binding.buttonnav
//                Plans_fragment().hide(buttonnav)
                binding.buttonnav.setOnNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.home_t -> makeCurrentFrag(Home_fragment)
                        R.id.plan -> makeCurrentFrag(plansFragment)
                        R.id.profile -> makeCurrentFrag(ProfileFragment)
                        R.id.stats -> makeCurrentFrag(Stats())

                    }
                    true
                }
            }
        }
//        registerReceiver(BoardCast(), IntentFilter(BackupDataOutput.ACTION_RESTORE_COMPLETE))
        // Request a backup of the specific preference and key
        val backupManager = BackupManager(this)
//        backupManager.dataChanged("step_count")

//    }
        userDitails.collection("fitness").document("health_report")
            .get().addOnSuccessListener { value ->
                if (value != null && value.exists()) {
                    val stat = value.data?.get("data").toString()
                    Constant.savedata(this,"health","stats",stat.toString())
                }
            }.addOnFailureListener {
            }
        Reset_AT_12_AM.setNotification(this)
}

    @SuppressLint("LongLogTag")
    override fun onStart() {
        super.onStart()
        if (!Constant.isServiceRunning(MyService::class.java, applicationContext)) {
            // Service is not running
            val intent = Intent(this, MyService::class.java).apply {
                action = MyService.ACTION_START
            }
            startService(intent)
        }
    }

    private fun makeCurrentFrag(Fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.left_center, R.anim.right_center);
            replace(R.id.frg, Fragment)
            commit()
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(VERSION_CODES.M)
    private fun checkpermission() {
        val allPermissionGranted = permissions.all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (!allPermissionGranted) {
            requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // All permissions granted
                } else {
                    // Permissions not granted
                }
            }
        }
    }

    @RequiresApi(VERSION_CODES.O)
    fun Getlatestweight() {
        userDitails.collection("Weight track").get().addOnSuccessListener { result ->
            val size = result.documents.size
            val curr_weight = result.documents[size - 1].get("weight").toString().toFloat()
            Constant.savedata(this, "weight", "curr_w", curr_weight.toString())
        }
    }
}

object Reset_AT_12_AM {
    @RequiresApi(VERSION_CODES.M)
    @SuppressLint("ServiceCast", "UnspecifiedImmutableFlag")
    fun setNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,Step_reset_BoardCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
    }
}