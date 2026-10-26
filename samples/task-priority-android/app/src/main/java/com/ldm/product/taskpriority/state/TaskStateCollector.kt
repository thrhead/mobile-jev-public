package com.ldm.product.taskpriority.state

import com.ldm.product.taskpriority.model.TaskDraft

object TaskStateCollector {
    fun collect(
        task: TaskDraft,
        nowEpochMillis: Long,
    ): Map<String, Any> {
        val state = linkedMapOf<String, Any>()

        val dueAt = task.dueAtEpochMillis
        when {
            dueAt == null -> {
                state["overdue_minutes"] = 0.0
            }

            dueAt > nowEpochMillis -> {
                state["deadline_minutes"] = (dueAt - nowEpochMillis).toDouble() / MILLIS_PER_MINUTE
                state["overdue_minutes"] = 0.0
            }

            dueAt < nowEpochMillis -> {
                state["deadline_minutes"] = 0.0
                state["overdue_minutes"] = (nowEpochMillis - dueAt).toDouble() / MILLIS_PER_MINUTE
            }

            else -> {
                state["deadline_minutes"] = 0.0
                state["overdue_minutes"] = 0.0
            }
        }

        state["importance"] = task.importance
        task.dependencyCount?.let { state["dependency_count"] = it.toDouble() }
        state["blocking_others"] = task.blockingOthers
        task.estimatedDurationMinutes?.let { state["estimated_duration_min"] = it.toDouble() }
        state["business_impact"] = task.businessImpact
        state["safety_impact"] = task.safetyImpact
        state["previous_delays"] = task.previousDelays.toDouble()
        task.requiredResourcesReady?.let { state["required_resources_ready"] = it }

        return state
    }

    private const val MILLIS_PER_MINUTE = 60_000.0
}
