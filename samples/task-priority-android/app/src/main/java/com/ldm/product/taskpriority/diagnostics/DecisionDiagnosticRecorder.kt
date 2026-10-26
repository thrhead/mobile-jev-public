package com.ldm.product.taskpriority.diagnostics

import com.ldm.product.taskpriority.data.DecisionTraceEntity
import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailability
import com.ldm.product.taskpriority.service.EvaluationResult

class DecisionDiagnosticRecorder {
    fun toTrace(
        taskId: Long,
        timestamp: Long,
        result: EvaluationResult,
    ): DecisionTraceEntity {
        return DecisionTraceEntity(
            taskId = taskId,
            timestamp = timestamp,
            applicationStatus = result.applicationStatus,
            ldmStatus = result.ldmStatus,
            ldmSelectedCandidate = result.ldmSelectedCandidate,
            ldmConfidence = result.ldmConfidence,
            ldmAbstentionReason = result.ldmAbstentionReason,
            ldmErrorCode = result.ldmErrorCode,
            displayedPriority = result.displayedPriority,
            prioritySource = result.prioritySource.name,
            semanticStateJson = semanticStateJson(result.semanticState),
            availabilityJson = availabilityJson(result.availability),
            probabilitiesJson = result.probabilitiesJson,
            runtimeIdentityJson = result.runtimeIdentityJson,
            ldmErrorMessage = result.ldmErrorMessage,
            ldmErrorRetryable = result.ldmErrorRetryable,
        )
    }

    private fun semanticStateJson(state: Map<String, Any>): String {
        return state.entries.joinToString(prefix = "{", postfix = "}") { (key, value) ->
            "\"${escape(key)}\":${jsonValue(value)}"
        }
    }

    private fun availabilityJson(value: TaskPriorityCandidateAvailability): String {
        return "{\"critical\":${value.critical},\"high\":${value.high},\"low\":${value.low},\"medium\":${value.medium}}"
    }

    private fun jsonValue(value: Any): String {
        return when (value) {
            is Boolean, is Number -> value.toString()
            else -> "\"${escape(value.toString())}\""
        }
    }

    private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
