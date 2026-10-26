package com.ldm.product.taskpriority.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class, DecisionTraceEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TaskPriorityDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun decisionTraceDao(): DecisionTraceDao
}
