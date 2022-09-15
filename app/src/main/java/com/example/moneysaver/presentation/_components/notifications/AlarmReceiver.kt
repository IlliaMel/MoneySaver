package com.example.moneysaver.presentation._components.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent



class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {



        val service = NotificationService(context)
        service.showNotification()
    }
}