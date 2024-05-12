package me.youfang.common.utils

import java.io.IOException

fun exe(vararg params: String, inputCallback: (String) -> Unit) {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    val reader = process.inputReader(Charsets.UTF_8)
    var line = StringBuilder()
    while (true) {
        val cInt = reader.read()
        val c = cInt.toChar()
        if (cInt == -1) break
        if (c == '\r' || c == 'n') {
            print(line.toString())
            inputCallback(line.toString())
            print(c)
            line = StringBuilder()
        } else {
            line.append(c)
        }
    }
    println()
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    if (process.exitValue() == 0) return
    throw IOException("exe error ${process.exitValue()}: ${params.joinToString(" ")}")
}

fun exe(vararg params: String): String {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    val consoleOutput = process.inputReader(Charsets.UTF_8).readText()
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    return consoleOutput
}