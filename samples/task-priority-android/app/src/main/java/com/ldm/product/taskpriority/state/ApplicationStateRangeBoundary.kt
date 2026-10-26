package com.ldm.product.taskpriority.state

import com.ldm.product.taskpriority.model.PriorityProvenance

data class ApplicationRangeFallback(
    val applicationStatus: String,
    val displayedPriority: String,
    val prioritySource: PriorityProvenance,
    val violation: SemanticStateRangeViolation,
)

object ApplicationStateRangeBoundary {
    fun fallback(violation: SemanticStateRangeViolation): ApplicationRangeFallback {
        return ApplicationRangeFallback(
            applicationStatus = "APPLICATION_STATE_OUT_OF_RANGE",
            displayedPriority = "medium",
            prioritySource = PriorityProvenance.APPLICATION_FALLBACK,
            violation = violation,
        )
    }
}
