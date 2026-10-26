package com.ldm.product.notificationtiming.routing

import com.ldm.runtime.LdmPublicDecisionStatus
import com.ldm.runtime.LdmPublicOutcome

sealed class RoutedOutcome {
    abstract val actionCandidate: String
    abstract val actionSource: String

    data class Decision(
        val selectedCandidateId: String,
        val confidence: Double,
    ) : RoutedOutcome() {
        override val actionCandidate: String = selectedCandidateId
        override val actionSource: String = "LDM_DECISION"
    }

    data class AbstainFallback(
        val abstentionReason: String,
        val fallbackCandidateId: String = "silent",
    ) : RoutedOutcome() {
        override val actionCandidate: String = fallbackCandidateId
        override val actionSource: String = "APPLICATION_FALLBACK"
    }

    data class ErrorFallback(
        val errorCode: String,
        val errorMessage: String,
        val retryable: Boolean,
        val fallbackCandidateId: String = "silent",
    ) : RoutedOutcome() {
        override val actionCandidate: String = fallbackCandidateId
        override val actionSource: String = "APPLICATION_FALLBACK"
    }
}

object DecisionOutcomeRouter {
    private val canonicalCandidates = setOf("delay", "show_now", "silent")

    fun route(outcome: LdmPublicOutcome): RoutedOutcome {
        return when (outcome) {
            is LdmPublicOutcome.Success -> {
                val result = outcome.result
                when (result.status) {
                    LdmPublicDecisionStatus.DECISION -> {
                        val selected = result.selectedCandidateId
                        if (selected != null && selected in canonicalCandidates) {
                            RoutedOutcome.Decision(
                                selectedCandidateId = selected,
                                confidence = result.confidence,
                            )
                        } else {
                            RoutedOutcome.ErrorFallback(
                                errorCode = "APPLICATION_OBSERVED_INVALID_DECISION",
                                errorMessage = "DECISION did not contain a canonical available notification_timing candidate.",
                                retryable = false,
                            )
                        }
                    }

                    LdmPublicDecisionStatus.ABSTAIN -> {
                        RoutedOutcome.AbstainFallback(
                            abstentionReason = result.abstention.reason.name,
                        )
                    }
                }
            }

            is LdmPublicOutcome.Failure -> {
                RoutedOutcome.ErrorFallback(
                    errorCode = outcome.error.code.name,
                    errorMessage = outcome.error.message,
                    retryable = outcome.error.retryable,
                )
            }
        }
    }
}
