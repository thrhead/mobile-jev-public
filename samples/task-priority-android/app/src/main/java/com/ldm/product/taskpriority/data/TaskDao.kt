package com.ldm.product.taskpriority.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun insert(task: TaskEntity): Long

    @Update
    fun update(task: TaskEntity): Int

    @Query("DELETE FROM tasks WHERE id = :taskId")
    fun deleteById(taskId: Long): Int

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getById(taskId: Long): TaskEntity?

    @Query(
        """
        SELECT * FROM tasks
        ORDER BY
            CASE displayed_priority
                WHEN 'critical' THEN 0
                WHEN 'high' THEN 1
                WHEN 'medium' THEN 2
                WHEN 'low' THEN 3
                ELSE 4
            END ASC,
            CASE
                WHEN due_at IS NOT NULL AND due_at < :nowEpochMillis THEN 0
                WHEN due_at IS NOT NULL THEN 1
                ELSE 2
            END ASC,
            CASE WHEN due_at IS NOT NULL AND due_at < :nowEpochMillis THEN due_at END DESC,
            CASE WHEN due_at IS NOT NULL AND due_at >= :nowEpochMillis THEN due_at END ASC,
            created_at ASC,
            id ASC
        """,
    )
    fun listOrdered(nowEpochMillis: Long): List<TaskEntity>
}
