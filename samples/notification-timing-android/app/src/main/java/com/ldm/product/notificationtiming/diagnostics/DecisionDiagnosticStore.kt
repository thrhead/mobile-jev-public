package com.ldm.product.notificationtiming.diagnostics

import android.content.Context
import com.ldm.product.notificationtiming.action.ActionExecution
import com.ldm.product.notificationtiming.policy.CandidateAvailability
import com.ldm.product.notificationtiming.routing.RoutedOutcome
import com.ldm.product.notificationtiming.state.SemanticStateRangeViolation
import com.ldm.runtime.LdmPublicOutcome
import org.json.JSONArray
import org.json.JSONObject

class DecisionDiagnosticStore(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun record(
        state: Map<String, Any>,
        availability: CandidateAvailability,
        ldmOutcome: LdmPublicOutcome,
        routedOutcome: RoutedOutcome,
        actionExecution: ActionExecution,
    ): String {
        val json = JSONObject()
            .put("timestamp_ms", System.currentTimeMillis())
            .put("domain", "notification_timing")
            .put("state", mapToJson(state))
            .put(
                "availability",
                JSONObject()
                    .put("delay", availability.delay)
                    .put("show_now", availability.showNow)
                    .put("silent", availability.silent),
            )
            .put("action_source", routedOutcome.actionSource)
            .put("action_candidate", routedOutcome.actionCandidate)
            .put("action_success", actionExecution.success)
            .put("action_detail", actionExecution.detail)

        when (ldmOutcome) {
            is LdmPublicOutcome.Success -> {
                val result = ldmOutcome.result

                json.put("ldm_status", result.status.name)
                    .put("ldm_selected_candidate", result.selectedCandidateId ?: JSONObject.NULL)
                    .put("ldm_confidence", result.confidence)
                    .put(
                        "ldm_runtime",
                        JSONObject()
                            .put("model_id", result.runtime.modelId)
                            .put("contract_version", result.runtime.contractVersion)
                            .put("protocol_sha256", result.runtime.protocolSha256),
                    )
                    .put(
                        "ldm_ood",
                        JSONObject()
                            .put("status", result.ood.status.name)
                            .put("score", result.ood.score ?: JSONObject.NULL),
                    )
                    .put(
                        "ldm_abstention",
                        JSONObject()
                            .put("abstain", result.abstention.abstain)
                            .put("reason", result.abstention.reason.name),
                    )
                    .put(
                        "ldm_probabilities",
                        JSONArray().apply {
                            result.probabilities.forEach { candidate ->
                                put(
                                    JSONObject()
                                        .put("candidate_id", candidate.candidateId)
                                        .put("available", candidate.available)
                                        .put("probability", candidate.probability),
                                )
                            }
                        },
                    )
            }

            is LdmPublicOutcome.Failure -> {
                json.put("ldm_status", "ERROR")
                    .put(
                        "ldm_error",
                        JSONObject()
                            .put("contract_version", ldmOutcome.error.contractVersion)
                            .put("code", ldmOutcome.error.code.name)
                            .put("message", ldmOutcome.error.message)
                            .put("retryable", ldmOutcome.error.retryable),
                    )
            }
        }

        when (routedOutcome) {
            is RoutedOutcome.Decision -> Unit

            is RoutedOutcome.AbstainFallback -> {
                json.put("fallback_source", "APPLICATION_FALLBACK")
                    .put("application_fallback_candidate", routedOutcome.fallbackCandidateId)
            }

            is RoutedOutcome.ErrorFallback -> {
                json.put("fallback_source", "APPLICATION_FALLBACK")
                    .put("application_fallback_candidate", routedOutcome.fallbackCandidateId)
            }
        }

        return persist(json)
    }

    fun recordApplicationError(
        error: Throwable,
        fallbackCandidateId: String,
        actionExecution: ActionExecution,
    ): String {
        val json = JSONObject()
            .put("timestamp_ms", System.currentTimeMillis())
            .put("domain", "notification_timing")
            .put("application_status", "APPLICATION_ERROR")
            .put(
                "application_error",
                JSONObject()
                    .put("type", error.javaClass.simpleName)
                    .put("message", error.message ?: error.javaClass.simpleName),
            )
            .put("action_source", "APPLICATION_FALLBACK")
            .put("action_candidate", fallbackCandidateId)
            .put("action_success", actionExecution.success)
            .put("action_detail", actionExecution.detail)
            .put("fallback_source", "APPLICATION_FALLBACK")
            .put("application_fallback_candidate", fallbackCandidateId)

        return persist(json)
    }

    fun recordApplicationStateOutOfRange(
        state: Map<String, Any>,
        violation: SemanticStateRangeViolation,
        fallbackCandidateId: String,
        actionExecution: ActionExecution,
    ): String {
        val json = JSONObject()
            .put("timestamp_ms", System.currentTimeMillis())
            .put("domain", "notification_timing")
            .put("application_status", "APPLICATION_STATE_OUT_OF_RANGE")
            .put("state", mapToJson(state))
            .put(
                "state_range_violation",
                JSONObject()
                    .put("field", violation.field)
                    .put("value", violation.value)
                    .put("minimum", violation.minimum)
                    .put("maximum", violation.maximum),
            )
            .put("action_source", "APPLICATION_FALLBACK")
            .put("action_candidate", fallbackCandidateId)
            .put("action_success", actionExecution.success)
            .put("action_detail", actionExecution.detail)
            .put("fallback_source", "APPLICATION_FALLBACK")
            .put("application_fallback_candidate", fallbackCandidateId)

        return persist(json)
    }

    fun lastTrace(): String? = preferences.getString(KEY_LAST_TRACE, null)

    private fun persist(json: JSONObject): String {
        val rendered = json.toString(2)
        preferences.edit().putString(KEY_LAST_TRACE, rendered).apply()
        return rendered
    }

    private fun mapToJson(values: Map<String, Any>): JSONObject {
        val json = JSONObject()
        values.forEach { (key, value) -> json.put(key, value) }
        return json
    }

    private companion object {
        const val PREFS_NAME = "ldm_notification_diagnostics"
        const val KEY_LAST_TRACE = "last_trace"
    }
}
