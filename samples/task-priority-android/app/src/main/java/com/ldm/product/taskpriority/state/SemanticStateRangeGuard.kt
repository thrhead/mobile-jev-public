package com.ldm.product.taskpriority.state

data class SemanticStateRangeViolation(
    val field: String,
    val value: Double,
)

object SemanticStateRangeGuard {
    fun validate(state: Map<String, Any>): SemanticStateRangeViolation? {
        for ((field, range) in NUMERIC_RANGES) {
            val raw = state[field] ?: continue
            val value = (raw as? Number)?.toDouble() ?: continue
            if (!value.isFinite() || value < range.start || value > range.endInclusive) {
                return SemanticStateRangeViolation(field = field, value = value)
            }
        }
        return null
    }

    private val NUMERIC_RANGES = linkedMapOf(
        "deadline_minutes" to 0.0..10_080.0,
        "importance" to 0.0..1.0,
        "dependency_count" to 0.0..20.0,
        "estimated_duration_min" to 1.0..480.0,
        "overdue_minutes" to 0.0..10_080.0,
        "business_impact" to 0.0..1.0,
        "safety_impact" to 0.0..1.0,
        "previous_delays" to 0.0..10.0,
    )
}
