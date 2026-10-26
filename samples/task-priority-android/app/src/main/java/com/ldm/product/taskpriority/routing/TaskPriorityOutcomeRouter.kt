package com.ldm.product.taskpriority.routing

import com.ldm.product.taskpriority.model.PriorityProvenance
import com.ldm.runtime.LdmPublicDecisionStatus
import com.ldm.runtime.LdmPublicOutcome

sealed class TaskPriorityRoutedOutcome {
    abstract val displayedPriority: String
    abstract val prioritySource: PriorityProvenance

    data class Decision(
        val selectedCandidateId: String,
        val confidence: Double,
    ) : TaskPriorityRoutedOutcome() {
        override val displayedPriority: String = selectedCandidateId
        override val prioritySource: PriorityProvenance = PriorityProvenance.LDM_DECISION
    }

    data class AbstainFallback(
        val abstentionReason: String,
        val fallbackCandidateId: String = "medium",
    ) : TaskPriorityRoutedOutcome() {
        override val displayedPriority: String = fallbackCandidateId
        override val prioritySource: PriorityProvenance = PriorityProvenance.APPLICATION_FALLBACK
    }

    data class ErrorFallback(
        val errorCode: String,
        val errorMessage: String,
        val retryable: Boolean,
        val fallbackCandidateId: String = "medium",
    ) : TaskPriorityRoutedOutcome() {
        override val displayedPriority: String = fallbackCandidateId
        override val prioritySource: PriorityProvenance = PriorityProvenance.APPLICATION_FALLBACK
    }

    data class InvalidDecisionFallback(
        val fallbackCandidateId: String = "medium",
    ) : TaskPriorityRoutedOutcome() {
        override val displayedPriority: String = fallbackCandidateId
        override val prioritySource: PriorityProvenance = PriorityProvenance.APPLICATION_FALLBACK
    }
}

object TaskPriorityOutcomeRouter {
    private val canonicalCandidates = setOf("critical", "high", "low", "medium")

    fun route(outcome: LdmPublicOutcome): TaskPriorityRoutedOutcome {
        return when (outcome) {
            is LdmPublicOutcome.Success -> {
                val result = outcome.result
                when (result.status) {
                    LdmPublicDecisionStatus.DECISION -> {
                        val selected = result.selectedCandidateId
                        if (selected != null && selected in canonicalCandidates) {
                            TaskPriorityRoutedOutcome.Decision(
                                selectedCandidateId = selected,
                                confidence = result.confidence,
                            )
                        } else {
                            TaskPriorityRoutedOutcome.InvalidDecisionFallback()
                        }
                    }

                    LdmPublicDecisionStatus.ABSTAIN -> {
                        TaskPriorityRoutedOutcome.AbstainFallback(
                            abstentionReason = result.abstention.reason.name,
                        )
                    }
                }
            }

            is LdmPublicOutcome.Failure -> {
                TaskPriorityRoutedOutcome.ErrorFallback(
                    errorCode = outcome.error.code.name,
                    errorMessage = outcome.error.message,
                    retryable = outcome.error.retryable,
                )
            }
        }
    }
}
