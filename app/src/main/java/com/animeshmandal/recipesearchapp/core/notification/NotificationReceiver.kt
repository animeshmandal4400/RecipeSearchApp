package com.animeshmandal.recipesearchapp.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.animeshmandal.recipesearchapp.R

class NotificationReceiver : BroadcastReceiver() {
    
    companion object {
        const val CHANNEL_ID = "recipe_reminders"
        const val NOTIFICATION_ID = 1
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val recipeTitle = intent.getStringExtra("recipe_title") ?: "Your favorite recipe"
        val recipeId = intent.getIntExtra("recipe_id", 0)
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time to cook $recipeTitle")
            .setContentText("Your recipe reminder is here!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(
                // Create pending intent to open recipe detail
                android.app.PendingIntent.getActivity(
                    context,
                    recipeId,
                    Intent(context, com.animeshmandal.recipesearchapp.presentation.MainActivity::class.java)
                        .putExtra("recipe_id", recipeId),
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
        
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}


