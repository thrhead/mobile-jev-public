package com.ldm.product.taskpriority

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.room.Room
import com.ldm.product.taskpriority.data.TaskEntity
import com.ldm.product.taskpriority.data.TaskPriorityDatabase
import com.ldm.product.taskpriority.data.TaskRepository
import com.ldm.product.taskpriority.error.ApplicationErrorBoundary
import com.ldm.product.taskpriority.ldm.LdmDecisionClient
import com.ldm.product.taskpriority.model.TaskDraft
import com.ldm.product.taskpriority.service.TaskInboxController
import com.ldm.product.taskpriority.service.TaskPriorityEvaluator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var database: TaskPriorityDatabase
    private lateinit var decisionClient: LdmDecisionClient
    private lateinit var controller: TaskInboxController

    private lateinit var content: LinearLayout
    private var editingTaskId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(this, TaskPriorityDatabase::class.java, DATABASE_NAME).build()
        decisionClient = LdmDecisionClient(this)
        val evaluator = TaskPriorityEvaluator(decisionClient::decide)
        controller = TaskInboxController(
            repository = TaskRepository(database),
            evaluate = { task, now -> ApplicationErrorBoundary.capture { evaluator.evaluate(task, now) } },
        )
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val padding = dp(16)
            setPadding(padding, padding, padding, padding)
        }
        setContentView(ScrollView(this).apply { addView(content) })
        showInbox(refreshAfterLoad = true)
    }

    override fun onDestroy() {
        executor.shutdownNow()
        decisionClient.close()
        database.close()
        super.onDestroy()
    }

    private fun showInbox(refreshAfterLoad: Boolean = false) {
        content.removeAllViews()
        content.addView(TextView(this).apply {
            text = "Task Priority Inbox"
            textSize = 24f
        })
        content.addView(Button(this).apply {
            text = "New Task"
            setOnClickListener {
                editingTaskId = null
                showTaskForm(null)
            }
        })
        content.addView(TextView(this).apply { text = "Loading..." })

        executor.execute {
            val tasks = runCatching { controller.list() }.getOrDefault(emptyList())
            runOnUiThread { renderInboxTasks(tasks) }
            if (refreshAfterLoad && tasks.isNotEmpty()) {
                tasks.forEach { task -> runCatching { controller.reevaluate(task.id) } }
                val refreshed = runCatching { controller.list() }.getOrDefault(tasks)
                runOnUiThread { renderInboxTasks(refreshed) }
            }
        }
    }

    private fun renderInboxTasks(tasks: List<TaskEntity>) {
        if (isFinishing || isDestroyed) return
        while (content.childCount > 2) content.removeViewAt(2)
        if (tasks.isEmpty()) {
            content.addView(TextView(this).apply { text = "No tasks yet." })
            return
        }
        tasks.forEach { task -> content.addView(taskCard(task)) }
    }

    private fun taskCard(task: TaskEntity): View {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(12), 0, dp(12))
            addView(TextView(this@MainActivity).apply {
                text = task.title
                textSize = 18f
            })
            addView(TextView(this@MainActivity).apply {
                text = "Priority: ${task.displayedPriority.uppercase(Locale.US)}"
            })
            addView(TextView(this@MainActivity).apply {
                text = "Source: ${TaskInboxController.provenanceLabel(task)}"
            })
            addView(TextView(this@MainActivity).apply {
                text = dueLabel(task)
            })
            addView(TextView(this@MainActivity).apply {
                text = "Evaluated: ${formatTime(task.evaluatedAt)}"
            })
            addView(Button(this@MainActivity).apply {
                text = "Edit"
                setOnClickListener {
                    editingTaskId = task.id
                    showTaskForm(task)
                }
            })
            addView(Button(this@MainActivity).apply {
                text = "Re-evaluate"
                setOnClickListener { runTaskAction { controller.reevaluate(task.id) } }
            })
            addView(Button(this@MainActivity).apply {
                text = "Delete"
                setOnClickListener { runTaskAction { controller.delete(task.id) } }
            })
        }
    }

    private fun showTaskForm(existing: TaskEntity?) {
        content.removeAllViews()
        content.addView(TextView(this).apply {
            text = if (existing == null) "New Task" else "Edit Task"
            textSize = 24f
        })

        val title = textInput("Title", existing?.title)
        val importance = numberInput("Importance 0..1", existing?.importance?.toString() ?: "0.5", decimal = true)
        val businessImpact = numberInput("Business impact 0..1", existing?.businessImpact?.toString() ?: "0.5", decimal = true)
        val safetyImpact = numberInput("Safety impact 0..1", existing?.safetyImpact?.toString() ?: "0.0", decimal = true)
        val dueAt = textInput("Due date/time yyyy-MM-dd HH:mm (optional)", existing?.dueAt?.let(::formatTime))
        val blockingOthers = CheckBox(this).apply {
            text = "Blocking other work"
            isChecked = existing?.blockingOthers ?: false
        }.also(content::addView)
        val estimatedDuration = numberInput("Estimated duration minutes (optional)", existing?.estimatedDurationMin?.toString(), decimal = false)
        val dependencyCount = numberInput("Dependency count (optional)", existing?.dependencyCount?.toString(), decimal = false)
        val resourcesReady = Spinner(this).apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Unknown", "Yes", "No"),
            )
            setSelection(
                when (existing?.requiredResourcesReady) {
                    true -> 1
                    false -> 2
                    null -> 0
                },
            )
        }.also(content::addView)
        val error = TextView(this).also(content::addView)

        content.addView(Button(this).apply {
            text = "Save and Prioritize"
            setOnClickListener {
                val draft = runCatching {
                    TaskDraft(
                        id = existing?.id,
                        title = title.text.toString().trim().also { require(it.isNotEmpty()) { "Title is required" } },
                        createdAtEpochMillis = existing?.createdAt ?: System.currentTimeMillis(),
                        dueAtEpochMillis = parseDueAt(dueAt.text.toString()),
                        importance = requiredDouble(importance, "Importance"),
                        dependencyCount = optionalInt(dependencyCount, "Dependency count"),
                        blockingOthers = blockingOthers.isChecked,
                        estimatedDurationMinutes = optionalInt(estimatedDuration, "Estimated duration"),
                        businessImpact = requiredDouble(businessImpact, "Business impact"),
                        safetyImpact = requiredDouble(safetyImpact, "Safety impact"),
                        previousDelays = existing?.previousDelays ?: 0,
                        requiredResourcesReady = when (resourcesReady.selectedItemPosition) {
                            1 -> true
                            2 -> false
                            else -> null
                        },
                    )
                }.getOrElse {
                    error.text = "Input error: ${it.message}"
                    return@setOnClickListener
                }
                error.text = "Saving..."
                executor.execute {
                    val result = runCatching { controller.saveAndEvaluate(draft) }
                    runOnUiThread {
                        if (result.isSuccess) showInbox() else error.text = "Application error: ${result.exceptionOrNull()?.message}"
                    }
                }
            }
        })
        content.addView(Button(this).apply {
            text = "Cancel"
            setOnClickListener { showInbox() }
        })
    }

    private fun runTaskAction(action: () -> Unit) {
        executor.execute {
            runCatching(action)
            runOnUiThread { showInbox() }
        }
    }

    private fun textInput(hintText: String, value: String?): EditText {
        return EditText(this).apply {
            hint = hintText
            value?.let(::setText)
        }.also(content::addView)
    }

    private fun numberInput(hintText: String, value: String?, decimal: Boolean): EditText {
        return EditText(this).apply {
            hint = hintText
            inputType = InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_NUMBER_FLAG_SIGNED or
                if (decimal) InputType.TYPE_NUMBER_FLAG_DECIMAL else 0
            value?.let(::setText)
        }.also(content::addView)
    }

    private fun requiredDouble(input: EditText, name: String): Double {
        return input.text.toString().trim().toDoubleOrNull()
            ?: throw IllegalArgumentException("$name must be numeric")
    }

    private fun optionalInt(input: EditText, name: String): Int? {
        val text = input.text.toString().trim()
        if (text.isEmpty()) return null
        return text.toIntOrNull() ?: throw IllegalArgumentException("$name must be an integer")
    }

    private fun parseDueAt(value: String): Long? {
        val text = value.trim()
        if (text.isEmpty()) return null
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US).apply { isLenient = false }
        return formatter.parse(text)?.time ?: throw IllegalArgumentException("Invalid due date/time")
    }

    private fun dueLabel(task: TaskEntity): String {
        val due = task.dueAt ?: return "No due date"
        val delta = due - System.currentTimeMillis()
        return if (delta < 0) "Overdue: ${formatTime(due)}" else "Due: ${formatTime(due)}"
    }

    private fun formatTime(epochMillis: Long): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date(epochMillis))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    companion object {
        const val DATABASE_NAME = "task-priority.db"
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm"
    }
}
