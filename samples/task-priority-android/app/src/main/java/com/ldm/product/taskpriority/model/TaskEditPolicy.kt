package com.ldm.product.taskpriority.model

object TaskEditPolicy {
    fun nextPreviousDelays(
        previousDueAtEpochMillis: Long?,
        newDueAtEpochMillis: Long?,
        currentPreviousDelays: Int,
    ): Int {
        return if (
            previousDueAtEpochMillis != null &&
            newDueAtEpochMillis != null &&
            newDueAtEpochMillis > previousDueAtEpochMillis
        ) {
            currentPreviousDelays + 1
        } else {
            currentPreviousDelays
        }
    }
}
