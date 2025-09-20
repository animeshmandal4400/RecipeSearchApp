package com.animeshmandal.recipesearchapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.animeshmandal.recipesearchapp.core.notification.NotificationReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeSearchApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
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
