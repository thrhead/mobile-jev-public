package com.ldm.product.taskpriority.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DecisionTraceDao {
    @Insert
    fun insert(trace: DecisionTraceEntity): Long

    @Query("SELECT COUNT(*) FROM decision_traces WHERE task_id = :taskId")
    fun countForTask(taskId: Long): Int

    @Query("SELECT * FROM decision_traces WHERE task_id = :taskId ORDER BY id ASC")
    fun listForTask(taskId: Long): List<DecisionTraceEntity>
}
