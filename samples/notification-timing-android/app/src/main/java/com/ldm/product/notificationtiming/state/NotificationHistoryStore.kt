package com.ldm.product.notificationtiming.state

import android.content.Context

class NotificationHistoryStore(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    @Synchronized
    fun countSince(cutoffEpochMillis: Long): Int {
        return readTimestamps().count { it >= cutoffEpochMillis }
    }

    @Synchronized
    fun recordIncoming(timestampEpochMillis: Long) {
        val cutoff = timestampEpochMillis - ONE_HOUR_MS
        val retained = readTimestamps()
            .filter { it >= cutoff }
            .plus(timestampEpochMillis)
        preferences.edit().putString(KEY_TIMESTAMPS, retained.joinToString(",")).apply()
    }

    private fun readTimestamps(): List<Long> {
        val raw = preferences.getString(KEY_TIMESTAMPS, null) ?: return emptyList()
        if (raw.isBlank()) return emptyList()

        return raw.split(',')
            .mapNotNull { it.toLongOrNull() }
            .sorted()
    }

    private companion object {
        const val PREFS_NAME = "ldm_notification_history"
        const val KEY_TIMESTAMPS = "incoming_timestamps"
        const val ONE_HOUR_MS = 60L * 60L * 1000L
    }
}
