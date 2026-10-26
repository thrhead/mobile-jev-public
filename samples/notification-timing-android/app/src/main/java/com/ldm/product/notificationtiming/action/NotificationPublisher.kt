package com.ldm.product.notificationtiming.action

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class NotificationPublisher(context: Context) {
    private val appContext = context.applicationContext
    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)

    fun ensureChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "LDM notification timing",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        channel.description = "Notifications routed by the LDM notification_timing product slice."
        notificationManager.createNotificationChannel(channel)
    }

    fun canNotify(): Boolean {
        if (!notificationManager.areNotificationsEnabled()) return false
        if (Build.VERSION.SDK_INT < 33) return true

        return appContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
    }

    fun show(title: String, message: String): Boolean {
        if (!canNotify()) return false

        ensureChannel()
        val notification = Notification.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(nextNotificationId(), notification)
        return true
    }

    private fun nextNotificationId(): Int {
        return (System.currentTimeMillis() and 0x7fffffff).toInt()
    }

    companion object {
        const val CHANNEL_ID = "ldm_notification_timing"
    }
}
