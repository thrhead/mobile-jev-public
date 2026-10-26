package com.ldm.product.taskpriority.service

import com.ldm.product.taskpriority.data.TaskEntity
import com.ldm.product.taskpriority.data.TaskRepository
import com.ldm.product.taskpriority.diagnostics.DecisionDiagnosticRecorder
import com.ldm.product.taskpriority.model.PriorityProvenance
import com.ldm.product.taskpriority.model.TaskDraft
import com.ldm.product.taskpriority.model.TaskEditPolicy

class TaskInboxController(
    private val repository: TaskRepository,
    private val evaluate: (TaskDraft, Long) -> EvaluationResult,
    private val nowEpochMillis: () -> Long = System::currentTimeMillis,
    private val diagnosticRecorder: DecisionDiagnosticRecorder = DecisionDiagnosticRecorder(),
) {
    fun saveAndEvaluate(draft: TaskDraft): Long {
        val now = nowEpochMillis()
        val existing = draft.id?.let(repository::getTask)
        val previousDelays = if (existing == null) {
            0
        } else {
            TaskEditPolicy.nextPreviousDelays(
                previousDueAtEpochMillis = existing.dueAt,
                newDueAtEpochMillis = draft.dueAtEpochMillis,
                currentPreviousDelays = existing.previousDelays,
            )
        }
        val adjusted = draft.copy(
            createdAtEpochMillis = existing?.createdAt ?: draft.createdAtEpochMillis,
            previousDelays = previousDelays,
        )
        val result = evaluate(adjusted, now)
        val entity = adjusted.toEntity(
            id = existing?.id ?: 0L,
            result = result,
            evaluatedAt = now,
        )
        val trace = diagnosticRecorder.toTrace(
            taskId = entity.id,
            timestamp = now,
            result = result,
        )
        return repository.saveEvaluatedTask(entity, trace)
    }

    fun reevaluate(taskId: Long): Long {
        val existing = requireNotNull(repository.getTask(taskId)) { "Task $taskId does not exist" }
        val draft = existing.toDraft()
        val now = nowEpochMillis()
        val result = evaluate(draft, now)
        val entity = draft.toEntity(
            id = taskId,
            result = result,
            evaluatedAt = now,
        )
        val trace = diagnosticRecorder.toTrace(
            taskId = taskId,
            timestamp = now,
            result = result,
        )
        return repository.saveEvaluatedTask(entity, trace)
    }

    fun delete(taskId: Long) {
        repository.deleteTask(taskId)
    }

    fun list(): List<TaskEntity> = repository.listOrdered(nowEpochMillis())

    private fun TaskDraft.toEntity(
        id: Long,
        result: EvaluationResult,
        evaluatedAt: Long,
    ): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            createdAt = createdAtEpochMillis,
            dueAt = dueAtEpochMillis,
            importance = importance,
            dependencyCount = dependencyCount,
            blockingOthers = blockingOthers,
            estimatedDurationMin = estimatedDurationMinutes,
            businessImpact = businessImpact,
            safetyImpact = safetyImpact,
            previousDelays = previousDelays,
            requiredResourcesReady = requiredResourcesReady,
            displayedPriority = result.displayedPriority,
            prioritySource = result.prioritySource.name,
            evaluatedAt = evaluatedAt,
        )
    }

    private fun TaskEntity.toDraft(): TaskDraft {
        return TaskDraft(
            id = id,
            title = title,
            createdAtEpochMillis = createdAt,
            dueAtEpochMillis = dueAt,
            importance = importance,
            dependencyCount = dependencyCount,
            blockingOthers = blockingOthers,
            estimatedDurationMinutes = estimatedDurationMin,
            businessImpact = businessImpact,
            safetyImpact = safetyImpact,
            previousDelays = previousDelays,
            requiredResourcesReady = requiredResourcesReady,
        )
    }

    companion object {
        fun provenanceLabel(task: TaskEntity): String {
            return if (task.prioritySource == PriorityProvenance.APPLICATION_FALLBACK.name) {
                "Application fallback"
            } else {
                "LDM decision"
            }
        }
    }
}
