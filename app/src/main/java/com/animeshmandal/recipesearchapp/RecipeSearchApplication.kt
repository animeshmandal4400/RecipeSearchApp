package com.animeshmandal.recipesearchapp

import android.app.Application
import com.animeshmandal.recipesearchapp.core.notification.NotificationChannelManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RecipeSearchApplication : Application() {
    
    @Inject
    lateinit var notificationChannelManager: NotificationChannelManager
    
    override fun onCreate() {
        super.onCreate()
        notificationChannelManager.createNotificationChannels()
    }
}
