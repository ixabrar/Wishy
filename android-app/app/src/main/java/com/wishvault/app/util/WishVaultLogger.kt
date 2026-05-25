package com.wishvault.app.util

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LogEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: String,
    val level: String,
    val tag: String,
    val message: String,
    val exception: String? = null
)

object WishVaultLogger {
    private const val TAG = "WishVaultLogger"
    private var logFile: File? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
    private val shortDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    fun init(context: Context) {
        try {
            val logsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            if (logsDir != null) {
                val logDir = File(logsDir, "logs")
                if (!logDir.exists()) {
                    logDir.mkdirs()
                }
                logFile = File(logDir, "wishvault_logs.txt")
                Log.d(TAG, "Logger initialized at: ${logFile?.absolutePath}")
                
                // Write session start
                i("SYSTEM", "=== NEW WISHVAULT SESSION ===")
            } else {
                Log.e(TAG, "Failed to access external files dir for logging.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize logger: ${e.message}")
        }
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
        appendState("INFO", tag, message, null)
        writeToFile("INFO", tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        val stackTrace = throwable?.let { Log.getStackTraceString(it) }
        val fullMessage = if (throwable != null) {
            "$message | Exception: ${throwable.message}\n$stackTrace"
        } else {
            message
        }
        Log.e(tag, fullMessage)
        appendState("ERROR", tag, message, stackTrace)
        writeToFile("ERROR", tag, fullMessage)
    }

    private fun appendState(level: String, tag: String, message: String, exception: String?) {
        val entry = LogEntry(
            timestamp = shortDateFormat.format(Date()),
            level = level,
            tag = tag,
            message = message,
            exception = exception
        )
        val currentList = _logs.value.toMutableList()
        currentList.add(entry)
        _logs.value = currentList
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }

    private fun writeToFile(level: String, tag: String, message: String) {
        val file = logFile ?: return
        try {
            val timestamp = dateFormat.format(Date())
            val logLine = "[$timestamp] [$level] [$tag]: $message\n"
            
            FileWriter(file, true).use { writer ->
                writer.append(logLine)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write to log file", e)
        }
    }
}
