package com.ldm.product.notificationtiming.validation

import android.content.Context
import android.os.Build
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class ProductValidationStore(context: Context) {
    private val appContext = context.applicationContext
    private val preferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    @Synchronized
    fun startSession(): String {
        val sessionId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        @Suppress("DEPRECATION")
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }

        val root = JSONObject()
            .put("schema_version", "1.0")
            .put("evidence_type", "ANDROID_PRODUCT_PHYSICAL_RUNTIME")
            .put("claim_scope", "PRODUCT_RUNTIME_EXECUTION_ONLY")
            .put("session_id", sessionId)
            .put("started_at_ms", now)
            .put("completed", false)
            .put(
                "device",
                JSONObject()
                    .put("manufacturer", Build.MANUFACTURER)
                    .put("model", Build.MODEL)
                    .put("sdk_int", Build.VERSION.SDK_INT)
                    .put("android_release", Build.VERSION.RELEASE),
            )
            .put(
                "application",
                JSONObject()
                    .put("package_name", appContext.packageName)
                    .put("version_name", packageInfo.versionName ?: JSONObject.NULL)
                    .put("version_code", versionCode),
            )
            .put("events", JSONArray())
            .put(
                "non_claims",
                JSONArray()
                    .put("ANDROID_GENERAL_MULTI_DEVICE_NOT_ESTABLISHED")
                    .put("IOS_PHYSICAL_VALIDATION_NOT_ESTABLISHED")
                    .put("PRODUCTION_READINESS_NOT_ESTABLISHED"),
            )

        preferences.edit()
            .putString(KEY_REPORT, root.toString())
            .putString(KEY_SESSION_ID, sessionId)
            .apply()

        appendEvent(
            sessionId,
            "SESSION_STARTED",
            JSONObject().put("source", "DEBUG_VALIDATION_ACTIVITY"),
        )

        return sessionId
    }

    @Synchronized
    fun appendEvent(sessionId: String, type: String, data: JSONObject) {
        val root = readRoot() ?: return
        if (root.optString("session_id") != sessionId) return

        root.getJSONArray("events").put(
            JSONObject()
                .put("timestamp_ms", System.currentTimeMillis())
                .put("type", type)
                .put("data", data),
        )

        preferences.edit().putString(KEY_REPORT, root.toString()).apply()
    }

    @Synchronized
    fun completeSession(sessionId: String) {
        val root = readRoot() ?: return
        if (root.optString("session_id") != sessionId) return

        root.put("completed", true)
            .put("completed_at_ms", System.currentTimeMillis())

        root.getJSONArray("events").put(
            JSONObject()
                .put("timestamp_ms", System.currentTimeMillis())
                .put("type", "SESSION_COMPLETED")
                .put("data", JSONObject()),
        )

        preferences.edit().putString(KEY_REPORT, root.toString()).apply()
    }

    fun activeSessionId(): String? = preferences.getString(KEY_SESSION_ID, null)

    fun report(pretty: Boolean = true): String? {
        val root = readRoot() ?: return null
        return if (pretty) root.toString(2) else root.toString()
    }

    private fun readRoot(): JSONObject? {
        val raw = preferences.getString(KEY_REPORT, null) ?: return null
        return runCatching { JSONObject(raw) }.getOrNull()
    }

    private companion object {
        const val PREFS_NAME = "ldm_product_physical_validation"
        const val KEY_REPORT = "report"
        const val KEY_SESSION_ID = "session_id"
    }
}
