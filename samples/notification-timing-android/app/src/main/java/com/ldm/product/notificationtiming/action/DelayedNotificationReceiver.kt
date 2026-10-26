package com.ldm.product.notificationtiming.action

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ldm.product.notificationtiming.validation.ProductValidationStore
import org.json.JSONObject

class DelayedNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "LDM Notification"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: return
        val published = NotificationPublisher(context).show(title, message)

        val validationSessionId = intent.getStringExtra(EXTRA_VALIDATION_SESSION_ID)
        if (validationSessionId != null) {
            ProductValidationStore(context).appendEvent(
                validationSessionId,
                "DELAY_RECEIVER_EXECUTION",
                JSONObject()
                    .put("notification_publish_attempted", true)
                    .put("notification_publish_success", published),
            )
        }
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_VALIDATION_SESSION_ID = "validation_session_id"
    }
}
