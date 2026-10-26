package com.ldm.product.taskpriority.service

import com.ldm.product.taskpriority.model.PriorityProvenance
import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailability
import com.ldm.runtime.LdmPublicOutcome

data class EvaluationResult(
    val semanticState: Map<String, Any>,
    val availability: TaskPriorityCandidateAvailability,
    val displayedPriority: String,
    val prioritySource: PriorityProvenance,
    val applicationStatus: String? = null,
    val ldmStatus: String? = null,
    val ldmSelectedCandidate: String? = null,
    val ldmConfidence: Double? = null,
    val ldmAbstentionReason: String? = null,
    val ldmErrorCode: String? = null,
    val ldmErrorMessage: String? = null,
    val ldmErrorRetryable: Boolean? = null,
    val probabilitiesJson: String? = null,
    val runtimeIdentityJson: String? = null,
    val ldmOutcome: LdmPublicOutcome? = null,
)
