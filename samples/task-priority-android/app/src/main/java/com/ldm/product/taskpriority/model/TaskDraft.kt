package com.ldm.product.taskpriority.model

data class TaskDraft(
    val id: Long?,
    val title: String,
    val createdAtEpochMillis: Long,
    val dueAtEpochMillis: Long?,
    val importance: Double,
    val dependencyCount: Int?,
    val blockingOthers: Boolean,
    val estimatedDurationMinutes: Int?,
    val businessImpact: Double,
    val safetyImpact: Double,
    val previousDelays: Int,
    val requiredResourcesReady: Boolean?,
)
