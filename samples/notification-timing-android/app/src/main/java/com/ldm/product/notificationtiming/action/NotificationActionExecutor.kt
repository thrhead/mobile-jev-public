package com.ldm.product.notificationtiming.action

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ldm.product.notificationtiming.model.NotificationDecisionInput

data class ActionExecution(
    val success: Boolean,
    val detail: String,
)

class NotificationActionExecutor(context: Context) {
    private val appContext = context.applicationContext
    private val alarmManager = appContext.getSystemService(AlarmManager::class.java)
    private val publisher = NotificationPublisher(appContext)

    fun execute(
        candidateId: String,
        input: NotificationDecisionInput,
        validationSessionId: String? = null,
    ): ActionExecution {
        return when (candidateId) {
            "show_now" -> {
                if (publisher.show(input.title, input.message)) {
                    ActionExecution(true, "Notification posted immediately.")
                } else {
                    ActionExecution(false, "Notification permission/settings changed before execution.")
                }
            }

            "delay" -> scheduleDelay(input, validationSessionId)
            "silent" -> ActionExecution(true, "No interruptive notification was posted.")
            else -> ActionExecution(false, "Unknown action candidate: " + candidateId)
        }
    }

    private fun scheduleDelay(
        input: NotificationDecisionInput,
        validationSessionId: String?,
    ): ActionExecution {
        if (!publisher.canNotify()) {
            return ActionExecution(false, "Notification delivery is not currently permitted.")
        }

        val intent = Intent(appContext, DelayedNotificationReceiver::class.java)
            .putExtra(DelayedNotificationReceiver.EXTRA_TITLE, input.title)
            .putExtra(DelayedNotificationReceiver.EXTRA_MESSAGE, input.message)

        if (validationSessionId != null) {
            intent.putExtra(
                DelayedNotificationReceiver.EXTRA_VALIDATION_SESSION_ID,
                validationSessionId,
            )
        }

        val requestCode = (System.currentTimeMillis() and 0x7fffffff).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + DELAY_MS,
            pendingIntent,
        )

        return ActionExecution(true, "Notification scheduled for 15 minutes later.")
    }

    private companion object {
        const val DELAY_MS = 15L * 60L * 1000L
    }
}
