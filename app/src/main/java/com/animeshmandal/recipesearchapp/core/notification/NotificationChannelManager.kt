package com.animeshmandal.recipesearchapp.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationChannelManager @Inject constructor(
    private val context: Context
) {
    
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val reminderChannel = NotificationChannel(
                NotificationReceiver.CHANNEL_ID,
                "Recipe Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for recipe cooking reminders"
                enableVibration(true)
                enableLights(true)
            }
            
            notificationManager.createNotificationChannel(reminderChannel)
        }
    }
}
