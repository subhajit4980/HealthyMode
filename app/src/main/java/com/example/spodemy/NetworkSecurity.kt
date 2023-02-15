package com.example.spodemy

    import android.app.Application
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
        }
    }
