package com.ldm.product.taskpriority.ldm

import android.content.Context
import com.ldm.product.taskpriority.policy.TaskPriorityCandidateAvailability
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
        availability: TaskPriorityCandidateAvailability,
    ): LdmPublicOutcome {
        return adapter.decide(TaskPriorityDecisionRequestFactory.create(state, availability))
    }

    override fun close() {
        runtime.close()
    }
}
