package com.ldm.product.taskpriority.policy

data class TaskPriorityCandidateAvailability(
    val critical: Boolean,
    val high: Boolean,
    val low: Boolean,
    val medium: Boolean,
)
