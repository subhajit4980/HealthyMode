package com.example.spodemy.Service

    import android.app.Application
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.content.Context
    import android.os.Build
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.firestore.FirebaseFirestore
    import com.google.firebase.firestore.Source
    import com.google.firebase.firestore.ktx.firestore
    import com.google.firebase.ktx.Firebase

class NetworkSecurity: Application() {
        override fun onCreate() {
            super.onCreate()
            FirebaseFirestore.getInstance().enableNetwork()
            FirebaseFirestore.getInstance().collection("user").get(Source.CACHE)
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
            {
                val channel= NotificationChannel( "Stepcount","Stepcount", NotificationManager.IMPORTANCE_LOW)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
