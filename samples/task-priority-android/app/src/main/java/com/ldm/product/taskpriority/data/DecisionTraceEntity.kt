package com.ldm.product.taskpriority.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "decision_traces",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("task_id")],
)
data class DecisionTraceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "task_id") val taskId: Long,
    val timestamp: Long,
    @ColumnInfo(name = "application_status") val applicationStatus: String?,
    @ColumnInfo(name = "ldm_status") val ldmStatus: String?,
    @ColumnInfo(name = "ldm_selected_candidate") val ldmSelectedCandidate: String?,
    @ColumnInfo(name = "ldm_confidence") val ldmConfidence: Double?,
    @ColumnInfo(name = "ldm_abstention_reason") val ldmAbstentionReason: String?,
    @ColumnInfo(name = "ldm_error_code") val ldmErrorCode: String?,
    @ColumnInfo(name = "displayed_priority") val displayedPriority: String,
    @ColumnInfo(name = "priority_source") val prioritySource: String,
    @ColumnInfo(name = "semantic_state_json") val semanticStateJson: String,
    @ColumnInfo(name = "availability_json") val availabilityJson: String,
    @ColumnInfo(name = "probabilities_json") val probabilitiesJson: String?,
    @ColumnInfo(name = "runtime_identity_json") val runtimeIdentityJson: String?,
    @ColumnInfo(name = "ldm_error_message") val ldmErrorMessage: String? = null,
    @ColumnInfo(name = "ldm_error_retryable") val ldmErrorRetryable: Boolean? = null,
)
