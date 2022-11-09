package com.andriyilliaandroidgeeks.moneysaver.presentation._components.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivity

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            Intent(context, MainActivity::class.java),
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setContentTitle("Money Saver")
            .setContentText("Don`t forget to add your money transactions :) ")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)

    }

    companion object {
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}