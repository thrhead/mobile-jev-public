package com.ldm.product.notificationtiming.ldm

import android.content.Context
import com.ldm.product.notificationtiming.policy.CandidateAvailability
import com.ldm.runtime.LdmPublicAdapter
import com.ldm.runtime.LdmPublicOutcome
import com.ldm.runtime.LdmRuntime
import java.io.Closeable

class LdmDecisionClient(context: Context) : Closeable {
    private val runtime = LdmRuntime(context.applicationContext)
    private val adapter = LdmPublicAdapter(runtime)

    init {
        runtime.load()
    }

    fun decide(
        state: Map<String, Any>,
        availability: CandidateAvailability,
    ): LdmPublicOutcome {
        val request = NotificationDecisionRequestFactory.create(state, availability)
        return adapter.decide(request)
    }

    override fun close() {
        runtime.close()
    }
}
