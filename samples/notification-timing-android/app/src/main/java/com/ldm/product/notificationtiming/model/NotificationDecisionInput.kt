package com.ldm.product.notificationtiming.model

data class NotificationDecisionInput(
    val title: String,
    val message: String,
    val activityType: String,
    val taskUrgency: Double,
    val createdAtEpochMillis: Long,
    val deadlineMinutes: Double?,
    val previouslyDelayed: Int,
    val userActive: Boolean?,
) {
    init {
        require(activityType in ACTIVITY_TYPES) { "Unsupported activity_type: " + activityType }
        require(taskUrgency in 0.0..1.0) { "task_urgency must be in 0..1" }
        require(deadlineMinutes == null || deadlineMinutes in 0.0..1440.0) {
            "deadline_minutes must be missing or in 0..1440"
        }
        require(previouslyDelayed in 0..10) { "previously_delayed must be in 0..10" }
    }

    companion object {
        val ACTIVITY_TYPES = listOf("stationary", "walking", "driving", "sleeping")
    }
}
