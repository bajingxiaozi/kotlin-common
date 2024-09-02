package me.youfang.common.ext

import java.io.IOException

fun exe(vararg params: String) {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    if (process.exitValue() != 0) throw IOException("exe error ${process.exitValue()}: ${params.joinToString(" ")}")
}

fun exe(vararg params: String, outputCallback: (String) -> Unit) {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    val reader = process.inputStream.bufferedReader()
    val line = StringBuilder()
    while (true) {
        val cInt = reader.read()
        val c = cInt.toChar()
        if (cInt == -1) break
        print(c)
        if (c == '\r' || c == '\n') {
            outputCallback(line.toString())
            line.clear()
        } else {
            line.append(c)
        }
    }
    println()
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    if (process.exitValue() != 0) throw IOException("exe error ${process.exitValue()}: ${params.joinToString(" ")}")
}

fun readCommand(vararg params: String): String {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    val consoleOutput = process.inputStream.bufferedReader().readText()
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    return consoleOutput
}