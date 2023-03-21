package com.example.HealthyMode.Notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.HealthyMode.R

const val NOTIFICATION_ID=4
const val channel_ID="channel"
const val titleExtra="titleExtra"
const val messageExtra="messageExtra"
class Notification:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification=NotificationCompat.Builder(context, channel_ID)
            .setSmallIcon(R.drawable.mainlogo)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        val manager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID,notification)
    }
}