package me.youfang.common.utils

import java.io.File

private fun exe(dir: File?, silent: Boolean, vararg commands: String) {
    lds("exe ${commands.joinToString(" ")}", "$dir")
    val process = ProcessBuilder().command(*commands).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).directory(dir).start()
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

fun readCommand(command: String): String {
    val process = Runtime.getRuntime().exec(command)
    val output = process.inputStream.bufferedReader().readText()
    process.waitFor()
    return output
}

fun exeSimple(command: String, silent: Boolean) {
    lds("exe", command)
    val process = Runtime.getRuntime().exec(command)
    val shutdownHook = object : Thread() {
        override fun run() {
            ld("process still running and now stopped", command)
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    process.inputStream.bufferedReader().lines().forEach {
        println(it)
    }
    process.errorStream.bufferedReader().lines().forEach {
        println(it)
    }
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    lde("exe")
    if (process.exitValue() != 0) {
        if (silent) {
            lw("exe error", command)
        } else throw IllegalStateException("exe error: $command ")
    }
}