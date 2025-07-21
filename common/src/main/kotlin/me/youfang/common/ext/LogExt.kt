package me.youfang.common.ext

import java.awt.Color
import java.io.File
import java.util.concurrent.Executors

private val logThread = Executors.newSingleThreadExecutor { Thread(it).apply { isDaemon = true } }
private val LOG_WIDTH: Int by lazy { TERMINAL_SIZE.columns.coerceAtMost(100).takeIf { it > 0 } ?: 100 }

data class LogStage(
    val startTime: Long = System.currentTimeMillis()
)

var logFile: File? = null

private fun logToFile(message: String) = logThread.execute { logFile?.appendText(message + "\n") }

fun d(head: String, message: Any?) {
    logToFile("$currentTimeForLog d\t$head $message")
    println("> $head: $message")
}

fun d(message: Any?) {
    logToFile("$currentTimeForLog d\t$message")
    println(message)
}

fun d(head: String, message: Any?, color: Color) {
    logToFile("$currentTimeForLog d\t$head $message")
    println("> $head: $message".toColorTerminalString(color))
}

fun w(head: String, message: Any?) = d(head, message, Color.RED)

fun ds(head: String, message: Any?): LogStage {
    logToFile("$currentTimeForLog ds\t$head $message")
    println()
    println("=".repeat(LOG_WIDTH))
    println("> $head: $message")
    println("-".repeat(LOG_WIDTH))
    return LogStage()
}

fun LogStage.de() {
    logToFile("$currentTimeForLog de")
    println("\n操作完成时间: $currentTimeReadable, 耗时${(System.currentTimeMillis() - startTime).formatDuration()}")
    println("=".repeat(LOG_WIDTH / 2))
    println()
}

fun overridePrint(message: String) {
    print("\u001b[2K")
    print("\r")
    print(message)
    System.out.flush()
}