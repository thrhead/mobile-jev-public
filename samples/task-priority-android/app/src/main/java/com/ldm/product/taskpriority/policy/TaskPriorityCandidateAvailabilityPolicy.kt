package com.ldm.product.taskpriority.policy

object TaskPriorityCandidateAvailabilityPolicy {
    fun evaluate(): TaskPriorityCandidateAvailability {
        return TaskPriorityCandidateAvailability(
            critical = true,
            high = true,
            low = true,
            medium = true,
        )
    }
}
