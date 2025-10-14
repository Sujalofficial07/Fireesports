package com.fireesports.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.fireesports.util.NotificationHelper

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        remoteMessage.notification?.let {
            val title = it.title ?: "Fire eSports"
            val body = it.body ?: ""
            
            when (remoteMessage.data["type"]) {
                "tournament" -> {
                    val tournamentId = remoteMessage.data["tournamentId"] ?: ""
                    NotificationHelper.showTournamentNotification(
                        this,
                        title,
                        body,
                        tournamentId
                    )
                }
                "wallet" -> {
                    NotificationHelper.showWalletNotification(this, title, body)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // Send token to backend
    }
}
