package me.youfang.common.ext

import java.awt.Color

private val LOG_WIDTH: Int by lazy { TERMINAL_SIZE.columns.coerceAtMost(100).takeIf { it > 0 } ?: 100 }

data class LogStage(
    val startTime: Long = System.currentTimeMillis()
)

fun d(head: String, message: Any?) {
    println("> $head: $message")
}

fun d(message: Any?) {
    println(message)
}

fun d(head: String, message: Any?, color: Color) {
    println("> $head: $message".toColorTerminalString(color))
}

fun w(head: String, message: Any?) = d(head, message, Color.RED)

fun ds(head: String, message: Any?): LogStage {
    println()
    println("=".repeat(LOG_WIDTH))
    println("> $head: $message")
    println("-".repeat(LOG_WIDTH))
    return LogStage()
}

fun LogStage.de() {
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