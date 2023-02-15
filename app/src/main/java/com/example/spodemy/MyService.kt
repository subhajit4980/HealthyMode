import android.app.ActivityManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentManager
import com.example.spodemy.Home.Home_screen
import com.example.spodemy.Home.fragment.Home_fragment
import com.example.spodemy.R

@Suppress("CAST_NEVER_SUCCEEDS")
class MyService : Service() {
//   private val myBinder=MyBinder()
//    private lateinit var  myFragment:Home_fragment
//    private lateinit var notification:Notification
//    inner  class MyBinder:Binder(){
//        fun getService():MyService{
//            return this@MyService
//        }
//    }
    override fun onBind(intent: Intent): IBinder? {
        // Return null as this is not a bound service
        return null
    }

//    override fun onUnbind(intent: Intent?): Boolean {
//        return super.onUnbind(intent)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onCreate() {
//        super.onCreate()
//        myFragment= Home_fragment()
//        startMyFragment()
//        createNotification()
//    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun startMyFragment() {
//        val fragmentManager = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
//            .appTasks[0].taskInfo.topActivity as FragmentManager
//        fragmentManager.beginTransaction().commit()
//    }
//    private fun createNotification() {
//        val intent = Intent(this, Home_fragment::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
//            .setSmallIcon(R.drawable.ic_launcher)
//            .setContentTitle("My App is running")
//            .setContentText("My Fragment is running in the background")
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(false)
//
//        notification = notificationBuilder.build()
//
//        startForeground(1, notification)
//    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the service as a foreground service
        startForeground(1, getNotification())
        return START_STICKY
    }

    private fun getNotification(): Notification {
        // Create a notification that will be used to keep the service running in the background
        val notificationIntent = Intent(this, Home_screen::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("My App")
            .setContentText("Running in background")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        return notification
    }
}