package com.ldm.product.notificationtiming

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import com.ldm.product.notificationtiming.action.NotificationActionExecutor
import com.ldm.product.notificationtiming.action.NotificationPublisher
import com.ldm.product.notificationtiming.diagnostics.DecisionDiagnosticStore
import com.ldm.product.notificationtiming.error.ApplicationErrorBoundary
import com.ldm.product.notificationtiming.ldm.LdmDecisionClient
import com.ldm.product.notificationtiming.model.NotificationDecisionInput
import com.ldm.product.notificationtiming.policy.CandidateAvailabilityPolicy
import com.ldm.product.notificationtiming.routing.DecisionOutcomeRouter
import com.ldm.product.notificationtiming.routing.RoutedOutcome
import com.ldm.product.notificationtiming.state.ApplicationStateRangeBoundary
import com.ldm.product.notificationtiming.state.NotificationHistoryStore
import com.ldm.product.notificationtiming.state.NotificationStateCollector
import com.ldm.product.notificationtiming.state.SemanticStateRangeGuard

class MainActivity : Activity() {
    private lateinit var historyStore: NotificationHistoryStore
    private lateinit var stateCollector: NotificationStateCollector
    private lateinit var availabilityPolicy: CandidateAvailabilityPolicy
    private lateinit var decisionClient: LdmDecisionClient
    private lateinit var actionExecutor: NotificationActionExecutor
    private lateinit var diagnosticStore: DecisionDiagnosticStore
    private lateinit var applicationErrorBoundary: ApplicationErrorBoundary
    private lateinit var applicationStateRangeBoundary: ApplicationStateRangeBoundary

    private lateinit var titleInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var activitySpinner: Spinner
    private lateinit var urgencySeek: SeekBar
    private lateinit var urgencyLabel: TextView
    private lateinit var deadlineInput: EditText
    private lateinit var previousDelaysInput: EditText
    private lateinit var decideButton: Button
    private lateinit var resultView: TextView
    private lateinit var traceView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyStore = NotificationHistoryStore(this)
        stateCollector = NotificationStateCollector(this, historyStore)
        availabilityPolicy = CandidateAvailabilityPolicy(this)
        decisionClient = LdmDecisionClient(this)
        actionExecutor = NotificationActionExecutor(this)
        diagnosticStore = DecisionDiagnosticStore(this)
        applicationErrorBoundary = ApplicationErrorBoundary(this)
        applicationStateRangeBoundary = ApplicationStateRangeBoundary(this)

        NotificationPublisher(this).ensureChannel()
        setContentView(buildUi())
        renderLastTrace()
    }

    override fun onDestroy() {
        decisionClient.close()
        super.onDestroy()
    }

    private fun buildUi(): View {
        val padding = (16 * resources.displayMetrics.density).toInt()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(padding, padding, padding, padding)
        }

        root.addView(TextView(this).apply {
            text = "LDM Notification Timing"
            textSize = 24f
        })

        root.addView(TextView(this).apply {
            text = "Frozen Android SDK ile delay / show_now / silent vertical slice"
            textSize = 14f
        })

        titleInput = EditText(this).apply {
            hint = "Bildirim başlığı"
            setText("LDM Notification")
        }
        root.addView(titleInput)

        messageInput = EditText(this).apply {
            hint = "Bildirim mesajı"
            setText("Bu bildirim LDM notification_timing kararıyla işlendi.")
        }
        root.addView(messageInput)

        root.addView(TextView(this).apply {
            text = "activity_type"
        })

        activitySpinner = Spinner(this).apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                NotificationDecisionInput.ACTIVITY_TYPES,
            )
        }
        root.addView(activitySpinner)

        urgencyLabel = TextView(this)
        root.addView(urgencyLabel)

        urgencySeek = SeekBar(this).apply {
            max = 100
            progress = 60
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    renderUrgency(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
        }
        root.addView(urgencySeek)
        renderUrgency(urgencySeek.progress)

        deadlineInput = EditText(this).apply {
            hint = "deadline_minutes (opsiyonel, 0..1440)"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        root.addView(deadlineInput)

        previousDelaysInput = EditText(this).apply {
            hint = "previously_delayed (0..10)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText("0")
        }
        root.addView(previousDelaysInput)

        val permissionButton = Button(this).apply {
            text = "Bildirim izni iste"
            setOnClickListener { requestNotificationPermissionIfNeeded() }
        }
        root.addView(permissionButton)

        decideButton = Button(this).apply {
            text = "LDM ile karar ver"
            setOnClickListener { decide() }
        }
        root.addView(decideButton)

        resultView = TextView(this).apply {
            textSize = 16f
            text = "Henüz karar yok."
            setPadding(0, padding, 0, padding / 2)
        }
        root.addView(resultView)

        root.addView(TextView(this).apply {
            text = "Son diagnostic trace"
            textSize = 16f
        })

        traceView = TextView(this).apply {
            setTextIsSelectable(true)
            textSize = 12f
        }
        root.addView(traceView)

        root.addView(Button(this).apply {
            text = "Son diagnostic trace'i paylaş"
            setOnClickListener { shareLastTrace() }
        })

        return ScrollView(this).apply {
            addView(root)
        }
    }

    private fun decide() {
        val input = try {
            readInput()
        } catch (error: IllegalArgumentException) {
            resultView.text = "Input error: " + error.message
            return
        }

        decideButton.isEnabled = false
        resultView.text = "LDM çalışıyor..."

        Thread {
            try {
                val state = stateCollector.collect(input)
                val rangeViolation = SemanticStateRangeGuard.firstViolation(state)

                if (rangeViolation != null) {
                    val handled = applicationStateRangeBoundary.handle(
                        state = state,
                        violation = rangeViolation,
                        input = input,
                    )
                    runOnUiThread {
                        resultView.text = "APPLICATION_STATE_OUT_OF_RANGE (" +
                            rangeViolation.field + "=" + rangeViolation.value +
                            ", expected " + rangeViolation.minimum + ".." + rangeViolation.maximum + ")" +
                            "\nAPPLICATION_FALLBACK -> " + handled.actionCandidate +
                            "\naction=" + handled.actionExecution.detail
                        traceView.text = handled.trace
                        decideButton.isEnabled = true
                    }
                } else {
                    val availability = availabilityPolicy.evaluate()
                    val outcome = decisionClient.decide(state, availability)
                    val routed = DecisionOutcomeRouter.route(outcome)
                    val execution = actionExecutor.execute(routed.actionCandidate, input)

                    historyStore.recordIncoming(System.currentTimeMillis())
                    val trace = diagnosticStore.record(
                        state = state,
                        availability = availability,
                        ldmOutcome = outcome,
                        routedOutcome = routed,
                        actionExecution = execution,
                    )

                    runOnUiThread {
                        resultView.text = renderOutcome(routed, execution.detail)
                        traceView.text = trace
                        decideButton.isEnabled = true
                    }
                }
            } catch (error: Throwable) {
                val handled = applicationErrorBoundary.handle(error, input)
                runOnUiThread {
                    resultView.text = "APPLICATION_ERROR (" +
                        (error.message ?: error.javaClass.simpleName) + ")" +
                        "\nAPPLICATION_FALLBACK -> " + handled.actionCandidate +
                        "\naction=" + handled.actionExecution.detail
                    traceView.text = handled.trace
                    decideButton.isEnabled = true
                }
            }
        }.start()
    }

    private fun readInput(): NotificationDecisionInput {
        val title = titleInput.text.toString().trim().ifEmpty { "LDM Notification" }
        val message = messageInput.text.toString().trim().ifEmpty {
            "LDM notification_timing event"
        }

        val deadlineText = deadlineInput.text.toString().trim()
        val deadline = if (deadlineText.isEmpty()) {
            null
        } else {
            deadlineText.toDoubleOrNull()
                ?: throw IllegalArgumentException("deadline_minutes sayı olmalı.")
        }

        val previousDelays = previousDelaysInput.text.toString().trim().toIntOrNull()
            ?: throw IllegalArgumentException("previously_delayed tam sayı olmalı.")

        return NotificationDecisionInput(
            title = title,
            message = message,
            activityType = activitySpinner.selectedItem.toString(),
            taskUrgency = urgencySeek.progress / 100.0,
            createdAtEpochMillis = System.currentTimeMillis(),
            deadlineMinutes = deadline,
            previouslyDelayed = previousDelays,
            userActive = null,
        )
    }

    private fun renderOutcome(routed: RoutedOutcome, executionDetail: String): String {
        return when (routed) {
            is RoutedOutcome.Decision -> {
                "LDM DECISION -> " + routed.selectedCandidateId +
                    "\nconfidence=" + routed.confidence +
                    "\naction=" + executionDetail
            }

            is RoutedOutcome.AbstainFallback -> {
                "LDM ABSTAIN (" + routed.abstentionReason + ")" +
                    "\nAPPLICATION_FALLBACK -> " + routed.fallbackCandidateId +
                    "\naction=" + executionDetail
            }

            is RoutedOutcome.ErrorFallback -> {
                "LDM ERROR (" + routed.errorCode + ")" +
                    "\n" + routed.errorMessage +
                    "\nAPPLICATION_FALLBACK -> " + routed.fallbackCandidateId +
                    "\naction=" + executionDetail
            }
        }
    }

    private fun renderUrgency(progress: Int) {
        urgencyLabel.text = "task_urgency = " + (progress / 100.0)
    }

    private fun renderLastTrace() {
        traceView.text = diagnosticStore.lastTrace() ?: "Henüz diagnostic kayıt yok."
    }

    private fun shareLastTrace() {
        val trace = diagnosticStore.lastTrace()
        if (trace == null) {
            resultView.text = "Paylaşılacak diagnostic trace yok."
            return
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_SUBJECT, "LDM notification_timing diagnostic trace")
            putExtra(Intent.EXTRA_TEXT, trace)
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                "Diagnostic trace paylaş",
            ),
        )
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_POST_NOTIFICATIONS,
            )
        } else {
            resultView.text = "Bildirim izni zaten kullanılabilir."
        }
    }

    private companion object {
        const val REQUEST_POST_NOTIFICATIONS = 1001
    }
}
