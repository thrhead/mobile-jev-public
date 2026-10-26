package com.ldm.product.notificationtiming.state

data class SemanticStateRangeViolation(
    val field: String,
    val value: Double,
    val minimum: Double,
    val maximum: Double,
)

object SemanticStateRangeGuard {
    private data class FrozenRange(
        val minimum: Double,
        val maximum: Double,
    )

    private val guardedRanges = linkedMapOf(
        "notification_count_10m" to FrozenRange(0.0, 20.0),
        "notification_count_60m" to FrozenRange(0.0, 100.0),
        "task_age_minutes" to FrozenRange(0.0, 1440.0),
    )

    fun firstViolation(state: Map<String, Any>): SemanticStateRangeViolation? {
        guardedRanges.forEach { (field, frozenRange) ->
            val value = (state[field] as? Number)?.toDouble() ?: return@forEach
            if (!value.isFinite() || value < frozenRange.minimum || value > frozenRange.maximum) {
                return SemanticStateRangeViolation(
                    field = field,
                    value = value,
                    minimum = frozenRange.minimum,
                    maximum = frozenRange.maximum,
                )
            }
        }

        return null
    }
}
