package com.ldm.product.taskpriority.error

import com.ldm.product.taskpriority.model.PriorityProvenance
import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailabilityPolicy
import com.ldm.product.taskpriority.service.EvaluationResult

object ApplicationErrorBoundary {
    fun capture(block: () -> EvaluationResult): EvaluationResult {
        return try {
            block()
        } catch (_: Throwable) {
            EvaluationResult(
                semanticState = emptyMap(),
                availability = TaskPriorityCandidateAvailabilityPolicy.evaluate(),
                displayedPriority = "medium",
                prioritySource = PriorityProvenance.APPLICATION_FALLBACK,
                applicationStatus = "APPLICATION_ERROR",
            )
        }
    }
}
