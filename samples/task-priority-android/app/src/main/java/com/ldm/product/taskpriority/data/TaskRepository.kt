package com.ldm.product.taskpriority.data

class TaskRepository(
    private val db: TaskPriorityDatabase,
) {
    private val taskDao = db.taskDao()
    private val traceDao = db.decisionTraceDao()

    fun saveEvaluatedTask(
        task: TaskEntity,
        trace: DecisionTraceEntity,
    ): Long {
        var resolvedTaskId = task.id
        db.runInTransaction {
            resolvedTaskId = if (task.id == 0L) {
                taskDao.insert(task)
            } else {
                require(taskDao.update(task) == 1) { "Task ${task.id} does not exist" }
                task.id
            }

            traceDao.insert(trace.copy(taskId = resolvedTaskId))
        }
        return resolvedTaskId
    }

    fun deleteTask(taskId: Long) {
        db.runInTransaction {
            taskDao.deleteById(taskId)
        }
    }

    fun getTask(taskId: Long): TaskEntity? = taskDao.getById(taskId)

    fun traceCount(taskId: Long): Int = traceDao.countForTask(taskId)

    fun tracesForTask(taskId: Long): List<DecisionTraceEntity> = traceDao.listForTask(taskId)

    fun listOrdered(nowEpochMillis: Long): List<TaskEntity> = taskDao.listOrdered(nowEpochMillis)
}
