package com.ldm.product.notificationtiming.policy

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

data class CandidateAvailability(
    val delay: Boolean,
    val showNow: Boolean,
    val silent: Boolean,
)

class CandidateAvailabilityPolicy(context: Context) {
    private val appContext = context.applicationContext
    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)

    fun evaluate(): CandidateAvailability {
        val canDeliverNotification = notificationManager.areNotificationsEnabled() &&
            hasPostNotificationsPermission()

        return CandidateAvailability(
            delay = canDeliverNotification,
            showNow = canDeliverNotification,
            silent = true,
        )
    }

    private fun hasPostNotificationsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < 33) return true

        return appContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
    }
}
