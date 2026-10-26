package com.ldm.product.taskpriority.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "due_at") val dueAt: Long?,
    val importance: Double,
    @ColumnInfo(name = "dependency_count") val dependencyCount: Int?,
    @ColumnInfo(name = "blocking_others") val blockingOthers: Boolean,
    @ColumnInfo(name = "estimated_duration_min") val estimatedDurationMin: Int?,
    @ColumnInfo(name = "business_impact") val businessImpact: Double,
    @ColumnInfo(name = "safety_impact") val safetyImpact: Double,
    @ColumnInfo(name = "previous_delays") val previousDelays: Int,
    @ColumnInfo(name = "required_resources_ready") val requiredResourcesReady: Boolean?,
    @ColumnInfo(name = "displayed_priority") val displayedPriority: String,
    @ColumnInfo(name = "priority_source") val prioritySource: String,
    @ColumnInfo(name = "evaluated_at") val evaluatedAt: Long,
)
