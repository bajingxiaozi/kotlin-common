package me.youfang.common.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.util.*

private fun exe(dir: File?, silent: Boolean, vararg commands: String) {
    lds("exe ${commands.joinToString(" ")}", "$dir")
    val fixCommands: List<String> = if (commands.size > 1) commands.toList() else (StringTokenizer(commands[0]) as Enumeration<String>).toList()
    val process = ProcessBuilder().command(fixCommands).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).directory(dir).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            ld("process still running and now stopped: ${commands.joinToString(" ")}", "$dir")
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    lde("exe")
    if (process.exitValue() != 0) {
        if (silent) {
            lw("exe error ${commands.joinToString("")}", "$dir")
        } else throw IllegalStateException("exe error: ${commands.joinToString(" ")} ->>> $dir")
    }
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

private fun BufferedReader.print() {
    while (true) {
        val nextChar = read()
        if (nextChar == -1) break
        print(nextChar)
    }
    System.out.flush()
}