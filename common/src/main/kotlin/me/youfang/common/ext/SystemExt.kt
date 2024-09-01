package me.youfang.common.ext

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

private val Process.utf8InputReader
    get() = BufferedReader(InputStreamReader(inputStream, "utf-8"))

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
    val reader = process.utf8InputReader
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
    val consoleOutput = process.utf8InputReader.readText()
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    return consoleOutput
}