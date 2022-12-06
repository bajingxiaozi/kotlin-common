package me.youfang.common.utils

import java.io.File

private fun exe(dir: File?, silent: Boolean, vararg commands: String) {
    ld("exe start ->>> ${commands.joinToString(" ")} ->>> $dir")
    val process = ProcessBuilder().command(*commands).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).directory(dir).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            ld("process still running and now stopped: ${commands.joinToString(" ")} ->>> $dir")
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    ld("<<<- exe end")
    if (process.exitValue() != 0) {
        if (silent) {
            lw("exe error: ${commands.joinToString("")} ->>> $dir")
        } else {
            throw IllegalStateException("exe error: ${commands.joinToString(" ")} ->>> $dir")
        }
    }
}

fun exe(vararg commands: String) = exe(null, false, *commands)

fun exe(dir: File?, vararg commands: String) = exe(dir, false, *commands)

fun exeSilent(dir: File?, vararg commands: String) = exe(dir, true, *commands)

fun readCommand(command: String): String {
    val process = Runtime.getRuntime().exec(command)
    val output = process.inputStream.bufferedReader().readText()
    process.waitFor()
    return output
}

fun exeSimple(command: String) {
    ld("exe start ->>> $command")
    val process = Runtime.getRuntime().exec(command)
    val shutdownHook = object : Thread() {
        override fun run() {
            ld("process still running and now stopped: $command")
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    val output = process.inputStream.bufferedReader().readText()
    println(output)
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    ld("<<<- exe end")
    if (process.exitValue() != 0) throw IllegalStateException("exe error: $command ")
}