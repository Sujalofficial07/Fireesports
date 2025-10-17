package com.fireesports

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FireESportsApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    "tournaments",
                    "Tournament Updates",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Updates about tournaments and matches"
                    enableLights(true)
                    enableVibration(true)
                },
                NotificationChannel(
                    "wallet",
                    "Wallet Transactions",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Wallet credits and debits"
                },
                NotificationChannel(
                    "general",
                    "General Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "General app notifications"
                }
            )

            val manager = getSystemService(NotificationManager::class.java)
            channels.forEach { channel ->
                manager.createNotificationChannel(channel)
            }
        }
    }
}
