package me.youfang.common.utils

import java.io.File
import java.util.*

private val Process.normalExit
    get() = exitValue() == 0

private fun exe(dir: File?, silent: Boolean, vararg commands: String) {
    val fixCommands: List<String> = if (commands.size > 1) commands.toList() else (StringTokenizer(commands[0]) as Enumeration<String>).toList()
    val commandReadable = fixCommands.joinToString(" ")
    lds(commandReadable, "$dir")
    val process = ProcessBuilder().command(fixCommands).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).directory(dir).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            ld("中断操作", "操作仍在执行中, 命令=$commandReadable, 目录=$dir")
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    lde(commandReadable)
    if (process.normalExit) return
    lw("操作执行失败!!!", "命令=$commandReadable, 目录=$dir")
    if (!silent) throw IllegalStateException("操作执行失败, 命令=$commandReadable, 目录=$dir")
}

fun exe(vararg commands: String) = exe(null, false, *commands)

fun exe(dir: File?, vararg commands: String) = exe(dir, false, *commands)

fun exeSilent(dir: File?, vararg commands: String) = exe(dir, true, *commands)

fun exeSilent(vararg commands: String) = exe(null, true, *commands)

fun readCommand(command: String): String {
    val process = Runtime.getRuntime().exec(command)
    val output = process.inputStream.bufferedReader().readText()
    process.waitFor()
    return output
}