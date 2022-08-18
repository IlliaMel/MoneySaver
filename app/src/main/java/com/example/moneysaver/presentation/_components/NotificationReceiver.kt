package com.example.moneysaver.presentation._components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val service = NotificationService(context)
        service.showNotification()
    }
}