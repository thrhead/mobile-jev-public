package com.ldm.product.taskpriority.ldm

import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailability
import com.ldm.runtime.LdmPublicCandidate
import com.ldm.runtime.LdmPublicDecisionRequest

object TaskPriorityDecisionRequestFactory {
    fun create(
        state: Map<String, Any>,
        availability: TaskPriorityCandidateAvailability,
    ): LdmPublicDecisionRequest {
        return LdmPublicDecisionRequest(
            "1.0",
            "task_priority",
            state,
            listOf(
                LdmPublicCandidate("critical", availability.critical),
                LdmPublicCandidate("high", availability.high),
                LdmPublicCandidate("low", availability.low),
                LdmPublicCandidate("medium", availability.medium),
            ),
        )
    }
}
