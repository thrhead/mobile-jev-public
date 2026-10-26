package com.ldm.product.taskpriority.service

import com.ldm.product.taskpriority.model.PriorityProvenance
import com.ldm.product.taskpriority.model.TaskDraft
import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailability
import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailabilityPolicy
import com.ldm.product.taskpriority.routing.TaskPriorityOutcomeRouter
import com.ldm.product.taskpriority.routing.TaskPriorityRoutedOutcome
import com.ldm.product.taskpriority.state.SemanticStateRangeGuard
import com.ldm.product.taskpriority.state.TaskStateCollector
import com.ldm.runtime.LdmPublicOutcome

class TaskPriorityEvaluator(
    private val decide: (Map<String, Any>, TaskPriorityCandidateAvailability) -> LdmPublicOutcome,
    private val availabilityProvider: () -> TaskPriorityCandidateAvailability = {
        TaskPriorityCandidateAvailabilityPolicy.evaluate()
    },
) {
    fun evaluate(task: TaskDraft, nowEpochMillis: Long): EvaluationResult {
        val state = TaskStateCollector.collect(task, nowEpochMillis)
        val violation = SemanticStateRangeGuard.validate(state)
        if (violation != null) {
            return EvaluationResult(
                semanticState = state,
                availability = TaskPriorityCandidateAvailabilityPolicy.evaluate(),
                displayedPriority = "medium",
                prioritySource = PriorityProvenance.APPLICATION_FALLBACK,
                applicationStatus = "APPLICATION_STATE_OUT_OF_RANGE",
            )
        }

        val availability = availabilityProvider()
        val outcome = decide(state, availability)
        val routed = TaskPriorityOutcomeRouter.route(outcome)

        return when (outcome) {
            is LdmPublicOutcome.Success -> {
                val result = outcome.result
                val common = EvaluationResult(
                    semanticState = state,
                    availability = availability,
                    displayedPriority = routed.displayedPriority,
                    prioritySource = routed.prioritySource,
                    ldmStatus = result.status.name,
                    ldmSelectedCandidate = result.selectedCandidateId,
                    ldmConfidence = result.confidence,
                    ldmAbstentionReason = result.abstention.reason.name,
                    probabilitiesJson = probabilitiesJson(outcome),
                    runtimeIdentityJson = runtimeIdentityJson(outcome),
                    ldmOutcome = outcome,
                )

                if (routed is TaskPriorityRoutedOutcome.InvalidDecisionFallback) {
                    common.copy(applicationStatus = "APPLICATION_OBSERVED_INVALID_DECISION")
                } else {
                    common
                }
            }

            is LdmPublicOutcome.Failure -> {
                EvaluationResult(
                    semanticState = state,
                    availability = availability,
                    displayedPriority = routed.displayedPriority,
                    prioritySource = routed.prioritySource,
                    ldmStatus = "ERROR",
                    ldmErrorCode = outcome.error.code.name,
                    ldmErrorMessage = outcome.error.message,
                    ldmErrorRetryable = outcome.error.retryable,
                    ldmOutcome = outcome,
                )
            }
        }
    }

    private fun probabilitiesJson(outcome: LdmPublicOutcome.Success): String {
        return outcome.result.probabilities.joinToString(prefix = "[", postfix = "]") { candidate ->
            "{\"candidate_id\":\"${escape(candidate.candidateId)}\",\"available\":${candidate.available},\"probability\":${candidate.probability}}"
        }
    }

    private fun runtimeIdentityJson(outcome: LdmPublicOutcome.Success): String {
        val runtime = outcome.result.runtime
        return "{\"model_id\":\"${escape(runtime.modelId)}\",\"contract_version\":\"${escape(runtime.contractVersion)}\",\"protocol_sha256\":\"${escape(runtime.protocolSha256)}\"}"
    }

    private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
