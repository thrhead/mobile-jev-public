package com.ldm.product.notificationtiming.error

import android.content.Context
import com.ldm.product.notificationtiming.action.ActionExecution
import com.ldm.product.notificationtiming.action.NotificationActionExecutor
import com.ldm.product.notificationtiming.diagnostics.DecisionDiagnosticStore
import com.ldm.product.notificationtiming.model.NotificationDecisionInput

data class ApplicationErrorHandlingResult(
    val status: String,
    val actionSource: String,
    val actionCandidate: String,
    val actionExecution: ActionExecution,
    val trace: String,
)

class ApplicationErrorBoundary(context: Context) {
    private val actionExecutor = NotificationActionExecutor(context)
    private val diagnosticStore = DecisionDiagnosticStore(context)

    fun handle(
        error: Throwable,
        input: NotificationDecisionInput,
    ): ApplicationErrorHandlingResult {
        val execution = actionExecutor.execute(FALLBACK_CANDIDATE, input)
        val trace = diagnosticStore.recordApplicationError(
            error = error,
            fallbackCandidateId = FALLBACK_CANDIDATE,
            actionExecution = execution,
        )

        return ApplicationErrorHandlingResult(
            status = APPLICATION_ERROR_STATUS,
            actionSource = APPLICATION_FALLBACK_SOURCE,
            actionCandidate = FALLBACK_CANDIDATE,
            actionExecution = execution,
            trace = trace,
        )
    }

    private companion object {
        const val APPLICATION_ERROR_STATUS = "APPLICATION_ERROR"
        const val APPLICATION_FALLBACK_SOURCE = "APPLICATION_FALLBACK"
        const val FALLBACK_CANDIDATE = "silent"
    }
}
