package com.ldm.product.notificationtiming.state

import android.content.Context
import com.ldm.product.notificationtiming.action.ActionExecution
import com.ldm.product.notificationtiming.action.NotificationActionExecutor
import com.ldm.product.notificationtiming.diagnostics.DecisionDiagnosticStore
import com.ldm.product.notificationtiming.model.NotificationDecisionInput

data class ApplicationStateRangeHandlingResult(
    val status: String,
    val actionSource: String,
    val actionCandidate: String,
    val actionExecution: ActionExecution,
    val trace: String,
)

class ApplicationStateRangeBoundary(context: Context) {
    private val actionExecutor = NotificationActionExecutor(context)
    private val diagnosticStore = DecisionDiagnosticStore(context)

    fun handle(
        state: Map<String, Any>,
        violation: SemanticStateRangeViolation,
        input: NotificationDecisionInput,
    ): ApplicationStateRangeHandlingResult {
        val execution = actionExecutor.execute(FALLBACK_CANDIDATE, input)
        val trace = diagnosticStore.recordApplicationStateOutOfRange(
            state = state,
            violation = violation,
            fallbackCandidateId = FALLBACK_CANDIDATE,
            actionExecution = execution,
        )

        return ApplicationStateRangeHandlingResult(
            status = APPLICATION_STATE_OUT_OF_RANGE_STATUS,
            actionSource = APPLICATION_FALLBACK_SOURCE,
            actionCandidate = FALLBACK_CANDIDATE,
            actionExecution = execution,
            trace = trace,
        )
    }

    private companion object {
        const val APPLICATION_STATE_OUT_OF_RANGE_STATUS = "APPLICATION_STATE_OUT_OF_RANGE"
        const val APPLICATION_FALLBACK_SOURCE = "APPLICATION_FALLBACK"
        const val FALLBACK_CANDIDATE = "silent"
    }
}
