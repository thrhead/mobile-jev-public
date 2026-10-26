package com.ldm.product.notificationtiming.state

import android.app.NotificationManager
import android.content.Context
import android.os.PowerManager
import com.ldm.product.notificationtiming.model.NotificationDecisionInput
import java.time.LocalDateTime

class NotificationStateCollector(
    context: Context,
    private val historyStore: NotificationHistoryStore,
) {
    private val appContext = context.applicationContext
    private val powerManager = appContext.getSystemService(PowerManager::class.java)
    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)

    fun collect(input: NotificationDecisionInput, nowEpochMillis: Long = System.currentTimeMillis()): Map<String, Any> {
        val taskAgeMinutes = (nowEpochMillis - input.createdAtEpochMillis).coerceAtLeast(0L) / 60_000.0
        val count10m = historyStore.countSince(nowEpochMillis - TEN_MINUTES_MS)
        val count60m = historyStore.countSince(nowEpochMillis - SIXTY_MINUTES_MS)

        val state = linkedMapOf<String, Any>(
            "hour_of_day" to LocalDateTime.now().hour.toDouble(),
            "screen_on" to powerManager.isInteractive,
            "activity_type" to input.activityType,
            "notification_count_10m" to count10m.toDouble(),
            "notification_count_60m" to count60m.toDouble(),
            "task_urgency" to input.taskUrgency,
            "task_age_minutes" to taskAgeMinutes,
            "previously_delayed" to input.previouslyDelayed.toDouble(),
        )

        input.userActive?.let {
            state["user_active"] = it
        }

        input.deadlineMinutes?.let {
            state["deadline_minutes"] = it
        }

        if (notificationManager.isNotificationPolicyAccessGranted) {
            state["device_in_dnd"] =
                notificationManager.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL
        }

        // user_active and headphones_connected are missing when the app does not know their semantic state.
        // The frozen encoder supports missing values, so the application does not guess them.
        return state
    }

    private companion object {
        const val TEN_MINUTES_MS = 10L * 60L * 1000L
        const val SIXTY_MINUTES_MS = 60L * 60L * 1000L
    }
}
