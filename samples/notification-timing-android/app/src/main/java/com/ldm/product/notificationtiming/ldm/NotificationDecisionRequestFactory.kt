package com.ldm.product.notificationtiming.ldm

import com.ldm.product.notificationtiming.policy.CandidateAvailability
import com.ldm.runtime.LdmPublicCandidate
import com.ldm.runtime.LdmPublicDecisionRequest

object NotificationDecisionRequestFactory {
    fun create(
        state: Map<String, Any>,
        availability: CandidateAvailability,
    ): LdmPublicDecisionRequest {
        return LdmPublicDecisionRequest(
            "1.0",
            "notification_timing",
            state,
            listOf(
                LdmPublicCandidate("delay", availability.delay),
                LdmPublicCandidate("show_now", availability.showNow),
                LdmPublicCandidate("silent", availability.silent),
            ),
        )
    }
}
